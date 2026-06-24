package com.itcast.myweb.enums;


import com.itcast.myweb.common.Constant;
import lombok.Getter;

/**
 * 查询订单时状态枚举类
 */
@Getter
public enum MyOrderStatus {

    //特殊
    ALL(Constant.ORDER_STATUS_ALL, "所有"),
    UNPAID(0, "待付款"),
    UNSHIPPED(1, "待发货"),
    UNRECEIVED(2, "待收货"),
    FINISHED(3, "已完成"),
    CANCELED(4, "已取消"),
    AFTER_SALES(5, "售后"),
    //特殊
    DELETED(Constant.ORDER_STATUS_DELETED, "已删除"),
    ;


    //状态
    private final Integer status;
    //描述
    private final String desc;


    MyOrderStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }


}
