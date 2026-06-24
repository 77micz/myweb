package com.itcast.myweb.common.exception;


/**
 * 描述：库存不足异常
 */
public class InsufficientStockException extends ItemException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
