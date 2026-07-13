package com.itcast.myweb.service.impl.client;

import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.*;
import com.itcast.myweb.domain.dto.PayDTO;
import com.itcast.myweb.domain.entity.*;
import com.itcast.myweb.enums.OrderStatus;
import com.itcast.myweb.enums.PayStatus;
import com.itcast.myweb.enums.PaymentType;
import com.itcast.myweb.mapper.OrderDetailMapper;
import com.itcast.myweb.service.client.PayService;
import com.itcast.myweb.service.common.IOrderService;
import com.itcast.myweb.service.common.IPayOrderService;
import com.itcast.myweb.service.common.IUserService;
import com.itcast.myweb.utils.TTLOffset;
import com.itcast.myweb.utils.UniqueID;
import com.itcast.myweb.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
     * stringRedisTemplate
     */
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 用户服务
     */
    private final IUserService userService;
    /**
     * rabbitTemplate
     */
    private final RabbitTemplate rabbitTemplate;


    /**
     * 生成支付单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createPayOrder(PayDTO payDTO) {

        //0.准备数据
        Long userId = UserHolder.get().getId();
        List<Long> orderIds = payDTO.getOrderIds();
//        PayType payType = payDTO.getPayType();
        String payChannelCode = payDTO.getPayChannelCode();
        PaymentType paymentType = payDTO.getPaymentType();


        //1.查询订单明细
        List<OrderDetail> orderDetails = orderDetailMapper.listByOrderIds(orderIds, userId, OrderStatus.PENDING_PAY.getStatus());

        //判断订单明细是否存在
        if (orderDetails == null || orderDetails.size() != orderIds.size()) {
            throw new OrderDetailMissException("订单明细缺失");
        }


        //2.写入支付单
        List<Long> payOrderNos = batchSavePayOrder(userId, orderDetails, payChannelCode, paymentType);

        //3.发送延迟消息
        for (Long payOrderNo : payOrderNos) {
            rabbitTemplate.convertAndSend(Constant.ORDER_TIMEOUT_EXCHANGE, Constant.ORDER_TIMEOUT_ROUTING_KEY, payOrderNo, message -> {
                //设置消息过期时间
                message.getMessageProperties().setDelay(TTLOffset.getRandomTTL(Constant.ORDER_TIMEOUT_TIME.longValue()).intValue());
                //设置用户id
                message.getMessageProperties().setHeader("userId", userId);
                return message;
            });
        }


    }


    /**
     * 支付
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pay(PayDTO payDTO) {

        // -----------------------------幂等性判断
        Long userId = UserHolder.get().getId();
        List<Long> payOrderNos = payDTO.getPayOrderNos();
        List<Long> ids = payDTO.getOrderIds();
        //判断是否为空
        if (payOrderNos == null || ids == null) {
            throw new IllegalArgumentException("没有要支付的订单");
        }
        //判断支付单数与订单数是否一致
        if (payOrderNos.size() != ids.size()) {
            throw new PayOrderNumException("订单数与支付单数不一致");
        }


        //判断支付单状态
        List<PayOrder> payOrderList = payOrderService.lambdaQuery()
                .in(PayOrder::getPayOrderNo, payOrderNos)
                .eq(PayOrder::getUserId, userId)
                .list();
        if (payOrderList == null || payOrderList.isEmpty()) {
            throw new PayOrderNotFoundException("支付单不存在");
        }
        //判断数量是否一致
        if (payOrderList.size() != payOrderNos.size()) {
            throw new PayOrderNumException("支付单缺失");
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


        //获取分布式锁
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(Constant.PAY_LOCK_KEY + payOrderNos, payOrderNos.toString(), Constant.PAY_LOCK_TTL, TimeUnit.SECONDS))) {
            throw new PayLockGetFailException("支付正在处理，请勿重复提交");
        }

        //获取总金额
        BigDecimal total = getTotalAmount(payOrderList, user);


        try {//只保护修改操作

            //1.支付
            userService.lambdaUpdate()
                    .eq(User::getId, userId)
                    .set(User::getBalance, user.getBalance().subtract(total))
                    .update();
            //2.更新支付单状态
            LocalDateTime now = LocalDateTime.now();
            payOrderService.lambdaUpdate()
                    .in(PayOrder::getPayOrderNo, payOrderNos)
                    .eq(PayOrder::getUserId, userId)
                    .set(PayOrder::getStatus, PayStatus.PAY_SUCCESS)
                    .set(PayOrder::getPaySuccessTime, now)
                    .set(PayOrder::getUpdateTime, now)
                    .update();

            //3.更新订单状态

            LocalDateTime updateTime = LocalDateTime.now();

            orderService.lambdaUpdate()
                    .eq(Order::getUserId, userId)
                    .in(Order::getId, ids)
                    .set(Order::getStatus, OrderStatus.PENDING_DELIVERY.getStatus())
                    .set(Order::getUpdateTime, updateTime)
                    .set(Order::getPayTime, updateTime)
                    .update();


        } finally {
            //释放分布式锁
            stringRedisTemplate.delete(Constant.PAY_LOCK_KEY + payOrderNos);
        }


    }


    /**
     * 取消支付
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelPay(PayDTO payDTO) {

        //0.准备数据
        Long userId = UserHolder.get().getId();
        List<Long> ids = payDTO.getOrderIds();
        //判断是否为空
        if (ids == null || ids.size() != 1) {
            throw new IllegalArgumentException("没有要取消的订单");
        }
        //查询支付单
        PayOrder payOrder = payOrderService.lambdaQuery()
                .eq(PayOrder::getUserId, userId)
                .eq(PayOrder::getPayOrderNo, ids.get(0))
                .one();
        //判断支付单是否存在
        if (payOrder == null) {
            throw new PayOrderNotFoundException("支付单不存在");
        }
        Long payOrderNo = payOrder.getPayOrderNo();


        //判断支付单状态是否为支付成功
        if (payOrder.getStatus().equals(PayStatus.PAY_SUCCESS)) {
            throw new PayOrderStatusException("支付单已支付，不能取消");
        }


        //1.更新支付单
        LocalDateTime now = LocalDateTime.now();
        payOrderService.lambdaUpdate()
                .eq(PayOrder::getPayOrderNo, payOrderNo)
                .eq(PayOrder::getBizOrderNo, ids.get(0))
                .eq(PayOrder::getUserId, userId)
                .set(PayOrder::getStatus, PayStatus.PAY_CANCEL)
                .set(PayOrder::getPayCancelTime, now)
                .set(PayOrder::getUpdateTime, now)
                .update();


    }


    /**
     * 删除支付单
     */
    @Override
    public void removePayOrder(PayDTO payDTO) {

        //0.准备数据
        Long userId = UserHolder.get().getId();
        List<Long> ids = payDTO.getOrderIds();
        //判断是否为空
        if (ids == null || ids.size() != 1) {
            throw new IllegalArgumentException("没有要删除的支付单");
        }

        //查询支付单
        PayOrder payOrder = payOrderService.lambdaQuery()
                .eq(PayOrder::getUserId, userId)
                .eq(PayOrder::getBizOrderNo, ids.get(0))
                .one();
        //判断支付单是否存在
        if (payOrder == null) {
            throw new PayOrderNotFoundException("支付单不存在");
        }


        //1.删除支付单
        payOrderService.removeById(payOrder.getId());


    }


    //保存支付单
    private List<Long> batchSavePayOrder(Long userId, List<OrderDetail> orderDetails, String payChannelCode, PaymentType paymentType) {
        //判断支付渠道是否为余额支付
        if (!paymentType.equals(PaymentType.BALANCE)) {
            throw new IllegalArgumentException("暂时不支持该支付方式");
        }
        //循环处理
        List<Long> payOrderNos = new ArrayList<>();
        List<PayOrder> payOrders = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            PayOrder payOrder = new PayOrder();
            //生成支付单号
            Long payOrderNo = uniqueID.getUniqueId(Constant.PAY_ORDER_UNIQUE_ID_CACHE_KEY_PREFIX);
            payOrderNos.add(payOrderNo);
            payOrder.setAmount(orderDetail.getActualPayment());
            payOrder.setPayOrderNo(payOrderNo);
            payOrder.setBizOrderNo(orderDetail.getOrderId());
            payOrder.setUserId(userId);
            payOrder.setPayChannelCode(payChannelCode);
            payOrder.setPaymentType(paymentType);
            payOrder.setStatus(PayStatus.PENDING_PAY);
            payOrders.add(payOrder);
        }

        //批量插入支付单
        payOrderService.saveBatch(payOrders);
        return payOrderNos;
    }


    //获取总金额
    private static BigDecimal getTotalAmount(List<PayOrder> payOrderList, User user) {
        BigDecimal total = BigDecimal.ZERO;

        for (PayOrder payOrder : payOrderList) {

            if (payOrder.getStatus().equals(PayStatus.PAY_SUCCESS)) {
                throw new PayException("已支付成功");
            } else if (payOrder.getStatus().equals(PayStatus.PAY_TIMEOUT)) {
                throw new PayException("支付超时");
            } else if (payOrder.getStatus().equals(PayStatus.PAY_CANCEL)) {
                throw new PayException("支付已取消");
            }

            BigDecimal amount = payOrder.getAmount();
            total = total.add(amount);


        }

        //判断用户余额是否足够
        if (user.getBalance().compareTo(total) < 0) {
            throw new UserBalanceNotEnoughException("余额不足");
        }
        return total;
    }


}
