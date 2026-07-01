package com.itcast.myweb.common.exception;

/**
 * 取消支付锁获取失败异常
 */
public class PayCancelLockGetFailException extends PayException {
    public PayCancelLockGetFailException(String message) {
        super(message);
    }
}
