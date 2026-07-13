package com.itcast.myweb.listener;


import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.PayOrderNumException;
import com.itcast.myweb.domain.dto.UserDTO;
import com.itcast.myweb.domain.entity.Order;
import com.itcast.myweb.domain.entity.PayOrder;
import com.itcast.myweb.enums.OrderStatus;
import com.itcast.myweb.enums.PayStatus;
import com.itcast.myweb.service.client.TradeService;
import com.itcast.myweb.service.common.IOrderService;
import com.itcast.myweb.service.common.IPayOrderService;
import com.itcast.myweb.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 延迟队列监听器
 */
@Component
@RequiredArgsConstructor
public class TimeOutListener {


    /**
     * 交易服务
     */
    private final TradeService tradeService;

    /**
     * 支付单服务
     */
    private final IPayOrderService payOrderService;

    /**
     * 订单服务
     */
    private final IOrderService orderService;


    /**
     * 监听延迟队列消息
     */
    @Transactional(rollbackFor = Exception.class)
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = Constant.ORDER_TIMEOUT_QUEUE, durable = "true"),
                    exchange = @Exchange(name = Constant.ORDER_TIMEOUT_EXCHANGE, delayed = "true", type = ExchangeTypes.DIRECT),
                    key = Constant.ORDER_TIMEOUT_ROUTING_KEY
            )})
    public void listen(Long payOrderNo, Message message) {

        //0.准备数据
        Long userId = (Long) message.getMessageProperties().getHeader("userId");
        //判断用户是否存在
        if (userId == null) {
            throw new NullPointerException("用户信息缺失");
        }

        //1.设置到用户上下文
        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .build();
        UserHolder.set(userDTO);


        try {
            //2.查询支付单号
            PayOrder payOrder = payOrderService.lambdaQuery()
                    .eq(PayOrder::getUserId, userId)
                    .eq(PayOrder::getPayOrderNo, payOrderNo)
                    .one();
            if (payOrder == null) {
                throw new PayOrderNumException("支付单不存在");
            }


            //3.判断支付单状态是否为待支付
            //是，更新订单状态为超时，并返还库存
            if (payOrder.getStatus() == PayStatus.PENDING_PAY) {

                //4.更新支付单状态为超时
                payOrder.setStatus(PayStatus.PAY_TIMEOUT);
                LocalDateTime now = LocalDateTime.now();
                payOrder.setPayOverTime(now);
                payOrder.setUpdateTime(now);
                payOrderService.updateById(payOrder);

                //5.取消订单
                tradeService.cancel(payOrder.getBizOrderNo());


            } else if (payOrder.getStatus() == PayStatus.PAY_TIMEOUT || payOrder.getStatus() == PayStatus.PAY_CANCEL) {
                //同步订单状态
                tradeService.cancel(payOrder.getBizOrderNo());
            } else {
                //已支付，同步订单状态
                LocalDateTime now = LocalDateTime.now();
                orderService.lambdaUpdate()
                        .eq(Order::getId, payOrder.getBizOrderNo())
                        .eq(Order::getStatus, OrderStatus.PENDING_PAY)
                        .eq(Order::getUserId, userId)
                        .set(Order::getStatus, OrderStatus.PENDING_DELIVERY)
                        .set(Order::getUpdateTime, now)
                        .set(Order::getPayTime, payOrder.getPaySuccessTime())
                        .update();
            }


        } finally {
            //6.清除用户上下文
            UserHolder.remove();
        }


    }


}
