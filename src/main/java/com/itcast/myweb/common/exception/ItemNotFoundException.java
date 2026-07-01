package com.itcast.myweb.common.exception;


/**
 * 商品不存在异常
 */
public class ItemNotFoundException extends ItemException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
