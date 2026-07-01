package com.itcast.myweb.common.exception;


/**
 * 描述：库存不足异常
 */
public class ItemStockInsufficientException extends ItemException {
    public ItemStockInsufficientException(String message) {
        super(message);
    }
}
