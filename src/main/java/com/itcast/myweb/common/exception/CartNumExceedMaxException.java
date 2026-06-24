package com.itcast.myweb.common.exception;


/**
 * 购物车商品数量超出最大限制
 */
public class CartNumExceedMaxException extends CartException {
    public CartNumExceedMaxException(String message) {
        super(message);
    }
}
