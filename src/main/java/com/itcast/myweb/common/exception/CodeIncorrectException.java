package com.itcast.myweb.common.exception;


//错误验证码
public class CodeIncorrectException extends CodeException {
    public CodeIncorrectException(String message) {
        super(message);
    }
}
