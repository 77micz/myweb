package com.itcast.myweb.enums;


import lombok.Getter;

/**
 * 购物车状态枚举类
 */
@Getter
public enum ItemStatus {


    //店铺关闭，不可购买
    STORE_CLOSED(0, "店铺关闭，不可购买"),
    //款式缺货
    STYLE_OUT_OF_STOCK(1, "款式缺货"),
    //商品已下架
    ITEM_OFF_SHELF(2, "商品已下架"),
    //商品正常
    ITEM_NORMAL(3, "商品正常"),
    ;

    private final Integer code;//状态码

    private final String desc;//描述

    ItemStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
