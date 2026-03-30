package com.itcast.myweb.common.exception;

/**
 * 令牌为空异常
 */
public class NullTokenException extends TokenException {
    public NullTokenException(String message) {
        super(message);
    }
}
