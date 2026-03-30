package com.itcast.myweb.common.exception;


//验证码异常
public class CodeException extends RuntimeException {



    public CodeException(String message) {
        super(message);
    }
}
