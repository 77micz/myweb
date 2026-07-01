package com.itcast.myweb.common.exception;

/**
 * 用户密码错误异常
 */
public class UserPasswordErrorException extends UserException {
    public UserPasswordErrorException(String message) {
        super(message);
    }
}
