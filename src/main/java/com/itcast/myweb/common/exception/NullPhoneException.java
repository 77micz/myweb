package com.itcast.myweb.common.exception;

//手机号为空异常
public class NullPhoneException extends PhoneException {
    public NullPhoneException(String message) {
        super(message);
    }
}
