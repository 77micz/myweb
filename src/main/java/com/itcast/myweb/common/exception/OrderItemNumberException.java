package com.itcast.myweb.common.exception;

/**
 * 订单商品数量异常
 */
public class OrderItemNumberException extends OrderException {
    public OrderItemNumberException(String message) {
        super(message);
    }
}
