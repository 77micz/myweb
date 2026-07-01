package com.itcast.myweb.common.exception;


/**
 * 订单数不一致异常
 */
public class OrderNumberInconsistentException extends OrderException {
    public OrderNumberInconsistentException(String message) {
        super(message);
    }
}
