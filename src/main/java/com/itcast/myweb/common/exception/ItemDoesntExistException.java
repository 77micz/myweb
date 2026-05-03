package com.itcast.myweb.common.exception;


/**
 * 商品不存在异常
 */
public class ItemDoesntExistException extends ItemException {
    public ItemDoesntExistException(String message) {
        super(message);
    }
}
