package com.itcast.myweb.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 支付状态枚举
 */
@Getter
public enum PayStatus {

    //待提交
    PENDING_SUBMIT(0, "待提交"),
    //待支付
    PENDING_PAY(1, "待支付"),
    //支付超时或取消
    PAY_TIMEOUT_OR_CANCEL(2, "支付超时或取消"),
    //支付成功
    PAY_SUCCESS(3, "支付成功"),
    ;

    @EnumValue//状态值
    private final int value;//状态值
    private final String desc;//描述


    /**
     * 构造方法
     * @param value 支付状态值
     * @param desc 支付状态描述
     */
    PayStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
