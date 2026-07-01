package com.itcast.myweb.common.exception;

/**
 * 支付锁获取失败异常
 */
public class PayLockGetFailException extends RuntimeException {
    public PayLockGetFailException(String message) {
        super(message);
    }
}
