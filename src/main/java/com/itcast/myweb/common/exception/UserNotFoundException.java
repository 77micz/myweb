package com.itcast.myweb.common.exception;

/**
 * 用户不存在异常
 */
public class UserNotFoundException extends UserException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
