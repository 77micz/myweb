package com.itcast.myweb.common.exception;


//错误验证码
public class IncorrectCodeException extends CodeException {
    public IncorrectCodeException(String message) {
        super(message);
    }
}
