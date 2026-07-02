package com.itcast.myweb.common.exception;

/**
 * 地址数量不一致异常
 */
public class AddressNumberInconsistentException extends AddressException {
    public AddressNumberInconsistentException(String message) {
        super(message);
    }
}
