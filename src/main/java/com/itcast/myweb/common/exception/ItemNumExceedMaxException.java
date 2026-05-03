package com.itcast.myweb.common.exception;


/**
 * 商品数量超出最大限制
 */
public class ItemNumExceedMaxException extends ItemException {
    public ItemNumExceedMaxException(String message) {
        super(message);
    }
}
