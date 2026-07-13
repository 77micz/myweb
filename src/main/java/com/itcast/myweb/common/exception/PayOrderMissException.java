package com.itcast.myweb.common.exception;

/**
 * 支付单缺失异常
 */
public class PayOrderMissException extends PayException {
    public PayOrderMissException(String message) {
        super(message);
    }
}
