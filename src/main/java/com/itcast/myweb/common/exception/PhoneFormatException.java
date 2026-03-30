package com.itcast.myweb.common.exception;

//手机号格式错误异常
public class PhoneFormatException extends PhoneException {
    public PhoneFormatException(String message) {
        super(message);
    }
}
