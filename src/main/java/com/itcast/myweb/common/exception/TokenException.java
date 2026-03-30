package com.itcast.myweb.common.exception;

/**
 * 令牌异常类
 */
public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
