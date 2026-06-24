package com.itcast.myweb.common.exception;


/**
 * 订单明细不存在异常
 */
public class OrderDetailNotFoundException extends OrderException {
    public OrderDetailNotFoundException(String message) {
        super(message);
    }
}
