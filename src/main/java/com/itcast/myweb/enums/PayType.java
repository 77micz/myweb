package com.itcast.myweb.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 支付类型枚举
 */
@Getter
public enum PayType{
    JSAPI(1, "网页支付JS"),
    MINI_APP(2, "小程序支付"),
    APP(3, "APP支付"),
    NATIVE(4, "扫码支付"),
    BALANCE(5, "余额支付"),
    ;


    @EnumValue//类型值
    private final int value;//类型值
    private final String desc;//描述

    /**
     * 构造方法
     * @param value 支付类型值
     * @param desc 支付类型描述
     */
    PayType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 判断是否等于指定支付类型值
     * @param value 支付类型值
     * @return 是否等于指定支付类型值
     */
    public boolean equalsValue(Integer value){
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }
}
