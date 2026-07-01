package com.itcast.myweb.common.exception;

/**
 * 用户余额不足异常
 */
public class UserBalanceNotEnoughException extends UserException {
    public UserBalanceNotEnoughException(String message) {
        super(message);
    }
}
