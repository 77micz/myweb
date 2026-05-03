package com.itcast.myweb.common.exception;

/**
 * 用户id不匹配异常
 */
public class UserIdDoesntMatchException extends UserException {
    public UserIdDoesntMatchException(String message) {
        super(message);
    }
}
