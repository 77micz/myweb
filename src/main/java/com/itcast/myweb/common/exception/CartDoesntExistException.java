package com.itcast.myweb.common.exception;


/**
 * 购物车不存在异常
 */
public class CartDoesntExistException extends CartException {
    public CartDoesntExistException(String message) {
        super(message);
    }
}
