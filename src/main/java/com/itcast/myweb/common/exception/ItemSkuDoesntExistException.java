package com.itcast.myweb.common.exception;


/**
 * 商品sku不存在异常
 */
public class ItemSkuDoesntExistException extends ItemException {
    public ItemSkuDoesntExistException(String message) {
        super(message);
    }
}
