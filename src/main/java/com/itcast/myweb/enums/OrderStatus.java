package com.itcast.myweb.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 订单状态
 */
@Getter
public enum OrderStatus {

    PENDING_PAY(0, "待付款"),
    PENDING_DELIVERY(1, "待发货"),
    PENDING_RECEIVE(2, "待收货"),
    COMPLETE(3, "已完成"),
    CANCEL(4, "已取消"),
    REFUND(5, "退款/售后"),
    ;

    @EnumValue//枚举值
    private final Integer status;//订单状态
    private final String desc;//描述


    /**
     * 构造方法
     * @param status 订单状态
     * @param desc 描述
     */
    OrderStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }



}
