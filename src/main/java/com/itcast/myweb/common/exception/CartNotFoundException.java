package com.itcast.myweb.common.exception;


/**
 * 购物车不存在异常
 */
public class CartNotFoundException extends CartException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
