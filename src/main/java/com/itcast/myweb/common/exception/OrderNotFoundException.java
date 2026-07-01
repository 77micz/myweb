package com.itcast.myweb.common.exception;


/**
 * 订单缺失异常
 */
public class OrderNotFoundException extends OrderException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
