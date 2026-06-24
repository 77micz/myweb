package com.itcast.myweb.common.exception;


/**
 * 订单不存在异常
 */
public class OrderDoesntExistException extends OrderException{
    public OrderDoesntExistException(String message) {
        super(message);
    }
}
