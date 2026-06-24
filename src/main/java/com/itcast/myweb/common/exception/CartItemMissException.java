package com.itcast.myweb.common.exception;

/**
 * 购物车商品缺失异常
 */
public class CartItemMissException extends CartException {
    public CartItemMissException(String message) {
        super(message);
    }
}
