package com.itcast.myweb.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 支付状态枚举
 */
@Getter
public enum PayStatus {

    //待支付
    PENDING_PAY(1, "待支付"),
    //支付超时
    PAY_TIMEOUT(2, "支付超时"),
    //支付取消
    PAY_CANCEL(3, "支付取消"),
    //支付成功
    PAY_SUCCESS(4, "支付成功"),
    ;

    @EnumValue//状态值
    private final int value;//状态值
    private final String desc;//描述


    /**
     * 构造方法
     *
     * @param value 支付状态值
     * @param desc  支付状态描述
     */
    PayStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
