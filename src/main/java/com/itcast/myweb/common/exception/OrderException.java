package com.itcast.myweb.common.exception;


/**
 * 订单异常
 */
public class OrderException extends RuntimeException {
    public OrderException(String message) {
        super(message);
    }
}
