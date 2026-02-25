package com.itcast.myweb.exception;

//验证码过期异常
public class CodeExpiredException extends CodeException {
    public CodeExpiredException(String message) {
        super(message);
    }
}
