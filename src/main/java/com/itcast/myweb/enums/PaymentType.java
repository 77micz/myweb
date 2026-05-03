package com.itcast.myweb.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 支付方式
 */
@Getter
public enum PaymentType {

    ALIPAY(1, "支付宝"),
    WECHAT(2, "微信"),
    BALANCE(3, "余额"),
    ;

    @EnumValue//枚举值
    private final Integer code;//支付方式
    private final String desc;//描述

    /**
     * 构造方法
     * @param code 支付方式
     * @param desc 描述
     */
    PaymentType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }




}
