package com.itcast.myweb.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 通知状态
 */
@Getter
public enum NotifyStatus {

    //未通知
    UNNOTIFY(0, "未通知"),
    //已通知
    NOTIFIED(1, "已通知"),
    //通知失败
    FAILED(2, "通知失败"),
    ;

    @EnumValue//枚举值
    private final Integer value;//通知状态
    private final String desc;//描述

    /**
     * 构造方法
     * @param value 通知状态
     * @param desc 描述
     */
    NotifyStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }




}
