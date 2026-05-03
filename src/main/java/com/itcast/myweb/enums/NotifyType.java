package com.itcast.myweb.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 通知方式
 */
@Getter
public enum NotifyType {

    sms(1, "短信"),
    message(2, "站内信"),
    both(3, "都来"),
    ;

    @EnumValue//枚举值
    private final Integer value;//通知方式
    private final String desc;//描述

    /**
     * 构造方法
     * @param value 通知方式
     * @param desc 描述
     */
    NotifyType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }



}
