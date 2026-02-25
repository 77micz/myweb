package com.itcast.myweb.exception;


//验证码为空异常
public class NullCodeException extends CodeException {
    public NullCodeException(String message) {
        super(message);
    }
}
