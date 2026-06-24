package com.itcast.myweb.common.exception;


/**
 * 地址不存在异常
 */
public class AddressDoesntExistException extends AddressException {
    public AddressDoesntExistException(String message) {
        super(message);
    }
}
