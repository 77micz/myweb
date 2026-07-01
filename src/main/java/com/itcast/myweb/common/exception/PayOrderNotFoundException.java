package com.itcast.myweb.common.exception;

/**
 * 支付单不存在异常
 */
public class PayOrderNotFoundException extends PayException {
    public PayOrderNotFoundException(String message) {
        super(message);
    }
}
