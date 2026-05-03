package com.itcast.myweb.common.exception;

/**
 * 用户异常
 */
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
