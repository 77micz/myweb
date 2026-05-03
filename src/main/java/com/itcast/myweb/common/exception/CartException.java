package com.itcast.myweb.common.exception;


/**
 * 购物车异常
 */
public class CartException extends RuntimeException {
    public CartException(String message) {
        super(message);
    }
}
