package com.itcast.myweb.common.exception;

/**
 * 支付异常
 */
public class PayException extends RuntimeException {
    public PayException(String message) {
        super(message);
    }
}
