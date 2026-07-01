package com.itcast.myweb.common.exception;


/**
 * 商品sku不存在异常
 */
public class ItemSkuNotFoundException extends ItemException {
    public ItemSkuNotFoundException(String message) {
        super(message);
    }
}
