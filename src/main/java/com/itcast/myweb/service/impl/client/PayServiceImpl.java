package com.itcast.myweb.service.impl.client;

import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.*;
import com.itcast.myweb.domain.dto.PayDTO;
import com.itcast.myweb.domain.entity.*;
import com.itcast.myweb.enums.OrderStatus;
import com.itcast.myweb.enums.PayStatus;
import com.itcast.myweb.enums.PayType;
import com.itcast.myweb.enums.PaymentType;
import com.itcast.myweb.mapper.OrderDetailMapper;
import com.itcast.myweb.service.client.PayService;
import com.itcast.myweb.service.common.IOrderService;
import com.itcast.myweb.service.common.IPayOrderRelationService;
import com.itcast.myweb.service.common.IPayOrderService;
import com.itcast.myweb.service.common.IUserService;
import com.itcast.myweb.utils.UniqueID;
import com.itcast.myweb.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 支付服务实现类
 */
@Service
@RequiredArgsConstructor
public class PayServiceImpl implements PayService {


    /**
     * 订单服务
     */
    private final IOrderService orderService;

    /**
     * 订单明细服务
     */
    private final OrderDetailMapper orderDetailMapper;

    /**
     * 处理支付单-订单关联表的线程池
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * 唯一id生成器
     */
    private final UniqueID uniqueID;

    /**
     * 支付单服务
     */
    private final IPayOrderService payOrderService;


    /**
     * 支付单关联表服务
     */
    private final IPayOrderRelationService payOrderRelationService;

    /**
     * stringRedisTemplate
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 用户服务
     */
    private final IUserService userService;


    /**
     * 生成支付单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createPayOrder(PayDTO payDTO) {

        //0.准备数据
        Long userId = UserHolder.get().getId();
        List<Long> orderIds = payDTO.getOrderIds();
        PayType payType = payDTO.getPayType();
        String payChannelCode = payDTO.getPayChannelCode();
        PaymentType paymentType = payDTO.getPaymentType();


        //1.查询订单明细
        List<OrderDetail> orderDetails = orderDetailMapper.listByOrderIds(orderIds, userId, OrderStatus.PENDING_PAY.getStatus());

        //判断订单明细是否存在
        if (orderDetails == null || orderDetails.size() != orderIds.size()) {
            throw new OrderDetailMissException("订单明细缺失");
        }


        //2.写入支付单与订单关联记录
        savePayOrderAndRelation(userId, orderDetails, payChannelCode, payType, paymentType);


    }


    /**
     * 支付
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pay(PayDTO payDTO) {

        // -----------------------------幂等性判断
        Long userId = UserHolder.get().getId();
        Long payOrderNo = payDTO.getPayOrderNo();
        //获取分布式锁
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(Constant.PAY_LOCK_KEY + payOrderNo, payOrderNo.toString(), Constant.PAY_LOCK_TTL, TimeUnit.SECONDS))) {
            throw new PayLockGetFailException("支付正在处理，请勿重复提交");
        }

        try {
            //判断支付单状态
            PayOrder payOrder = payOrderService.lambdaQuery()
                    .eq(PayOrder::getPayOrderNo, payOrderNo)
                    .eq(PayOrder::getUserId, userId)
                    .one();
            if (payOrder == null) {
                throw new PayOrderNotFoundException("支付单不存在");
            }

            if (payOrder.getStatus().equals(PayStatus.PAY_SUCCESS)) {
                throw new PayException("已支付成功");
            } else if (payOrder.getStatus().equals(PayStatus.PAY_TIMEOUT)) {
                throw new PayException("支付超时");
            } else if (payOrder.getStatus().equals(PayStatus.PAY_CANCEL)) {
                throw new PayException("支付已取消");
            }

            //0.准备数据
            String payPw = payDTO.getPayPw();
            PaymentType paymentType = payDTO.getPaymentType();

            //判断支付方式选择调用对应支付渠道
            if (paymentType.equals(PaymentType.ALIPAY) || paymentType.equals(PaymentType.WECHAT)) {
                throw new IllegalArgumentException("暂不支持此支付方式");
            }


            //查询用户
            User user = userService.getById(userId);
            if (user == null) {
                throw new UserNotFoundException("用户不存在");
            }

            //判断支付密码是否正确
            if (!user.getPayPw().equals(payPw)) {
                throw new UserPasswordErrorException("支付密码错误，请重新输入");
            }

            //判断用户余额是否足够
            if (user.getBalance().compareTo(payOrder.getAmount()) < 0) {
                throw new UserBalanceNotEnoughException("余额不足");
            }

            //1.更新用户余额
            userService.lambdaUpdate()
                    .eq(User::getId, userId)
                    .set(User::getBalance, user.getBalance().subtract(payOrder.getAmount()))
                    .update();
            //2.更新支付单状态
            LocalDateTime now = LocalDateTime.now();
            payOrderService.lambdaUpdate()
                    .eq(PayOrder::getPayOrderNo, payOrderNo)
                    .eq(PayOrder::getUserId, userId)
                    .set(PayOrder::getStatus, PayStatus.PAY_SUCCESS)
                    .set(PayOrder::getPaySuccessTime, now)
                    .set(PayOrder::getUpdateTime, now)
                    .update();

            //3.更新订单状态
            //查询支付单关联的订单id
            List<Long> orderIds = payOrderRelationService.lambdaQuery()
                    .select(PayOrderRelation::getBizOrderNo)
                    .eq(PayOrderRelation::getPayOrderNo, payOrderNo)
                    .list().stream().map(PayOrderRelation::getBizOrderNo).collect(Collectors.toList());

            LocalDateTime updateTime = LocalDateTime.now();
            //判断订单数是否一致
            Long count = orderService.lambdaQuery()
                    .eq(Order::getUserId, userId)
                    .in(Order::getId, orderIds)
                    .count();
            if (count != orderIds.size()) {
                throw new OrderNumberInconsistentException("订单数不一致");
            }

            //更新订单状态
            orderService.lambdaUpdate()
                    .eq(Order::getUserId, userId)
                    .in(Order::getId, orderIds)
                    .set(Order::getStatus, OrderStatus.PENDING_DELIVERY.getStatus())
                    .set(Order::getUpdateTime, updateTime)
                    .set(Order::getPayTime, updateTime)
                    .update();
        } finally {
            //释放分布式锁
            stringRedisTemplate.delete(Constant.PAY_LOCK_KEY + payOrderNo);
        }


    }


    /**
     * 取消支付
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelPay(PayDTO payDTO) {

        // -----------------------------幂等性判断

        //0.准备数据
        Long userId = UserHolder.get().getId();
        Long payOrderNo = payDTO.getPayOrderNo();
        //获取分布式锁
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(Constant.PAY_CANCEL_LOCK_KEY + payOrderNo, payOrderNo.toString(), Constant.PAY_CANCEL_LOCK_TTL, TimeUnit.SECONDS))) {
            throw new PayCancelLockGetFailException("取消支付正在处理，请勿重复提交");
        }

        try {

            //获取支付单
            PayOrder payOrder = payOrderService.lambdaQuery()
                    .eq(PayOrder::getPayOrderNo, payOrderNo)
                    .eq(PayOrder::getUserId, userId)
                    .one();
            if (payOrder == null) {
                throw new PayOrderNotFoundException("支付单不存在");
            }
            //判断支付单状态是否为待支付
            if (!payOrder.getStatus().equals(PayStatus.PENDING_PAY)) {
                throw new PayOrderStatusException("支付单状态异常，不能取消");
            }


            //查询关联的订单
            List<Long> list = payOrderRelationService.lambdaQuery()
                    .select(PayOrderRelation::getBizOrderNo)
                    .eq(PayOrderRelation::getPayOrderNo, payOrderNo)
                    .list().stream().map(PayOrderRelation::getBizOrderNo).collect(Collectors.toList());

            //判断订单数是否一致
            Long count = orderService.lambdaQuery()
                    .eq(Order::getUserId, userId)
                    .eq(Order::getStatus, OrderStatus.PENDING_PAY.getStatus())
                    .in(Order::getId, list)
                    .count();
            if (count != list.size()) {
                throw new OrderNumberInconsistentException("订单数不一致");
            }


            //1.更新支付单
            LocalDateTime now = LocalDateTime.now();
            payOrderService.lambdaUpdate()
                    .eq(PayOrder::getPayOrderNo, payOrderNo)
                    .eq(PayOrder::getUserId, userId)
                    .set(PayOrder::getStatus, PayStatus.PAY_CANCEL)
                    .set(PayOrder::getPayCancelTime, now)
                    .set(PayOrder::getUpdateTime, now)
                    .update();

            //2.更新订单状态
            LocalDateTime updateTime = LocalDateTime.now();
            orderService.lambdaUpdate()
                    .eq(Order::getUserId, userId)
                    .in(Order::getId, list)
                    .set(Order::getStatus, OrderStatus.CANCEL.getStatus())
                    .set(Order::getCompleteTime, updateTime)
                    .set(Order::getCancelTime, updateTime)
                    .set(Order::getUpdateTime, updateTime)
                    .update();


        } finally {
            //释放锁
            stringRedisTemplate.delete(Constant.PAY_CANCEL_LOCK_KEY + payOrderNo);
        }

    }


    //3.保存支付单
    private void savePayOrderAndRelation(Long userId, List<OrderDetail> orderDetails, String payChannelCode, PayType payType, PaymentType paymentType) {
        PayOrder payOrder = new PayOrder();
        //判断支付渠道是否为余额支付
        Long payOrderId = null;
        if (paymentType.equals(PaymentType.BALANCE)) {
            //生成支付单号
            payOrderId = uniqueID.getUniqueId(Constant.PAY_ORDER_UNIQUE_ID_CACHE_KEY_PREFIX);
        }
        //计算总金额
        List<PayOrderRelation> payOrderRelations = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetails) {
            BigDecimal actualPayment = orderDetail.getActualPayment();
            //创建支付单关联记录
            payOrderRelations.add(getPayOrderRelation(orderDetail, payOrderId, actualPayment));
            totalAmount = totalAmount.add(actualPayment);
        }
        payOrder.setPayOrderNo(payOrderId);
        payOrder.setUserId(userId);
        payOrder.setAmount(totalAmount);
        payOrder.setPayChannelCode(payChannelCode);
        payOrder.setPayType(payType);
        payOrder.setStatus(PayStatus.PENDING_PAY);
        //批量插入支付单关联表
        payOrderRelationService.saveBatch(payOrderRelations);
        payOrderService.save(payOrder);
    }


    //创建支付单与业务订单关联记录
    private static PayOrderRelation getPayOrderRelation(OrderDetail orderDetail, Long payOrderId, BigDecimal actualPayment) {
        PayOrderRelation payOrderRelation = new PayOrderRelation();
        payOrderRelation.setPayOrderNo(payOrderId);
        payOrderRelation.setBizOrderNo(orderDetail.getOrderId());
        payOrderRelation.setAmount(actualPayment);
        return payOrderRelation;
    }
}
