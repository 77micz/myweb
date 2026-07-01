package com.itcast.myweb.common.exception;


/**
 * 地址不存在异常
 */
public class AddressNotFoundException extends AddressException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}
