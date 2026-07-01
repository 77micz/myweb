package com.itcast.myweb.common.exception;


/**
 * 订单明细不存在异常
 */
public class OrderDetailMissException extends OrderException {
    public OrderDetailMissException(String message) {
        super(message);
    }
}
