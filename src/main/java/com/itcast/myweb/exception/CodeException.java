package com.itcast.myweb.exception;


//验证码异常
public class CodeException extends RuntimeException {
    public CodeException(String message) {
        super(message);
    }
}
