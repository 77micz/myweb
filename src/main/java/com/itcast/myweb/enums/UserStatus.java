package com.itcast.myweb.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Objects;

/**
 * 用户状态
 */
@Getter
public enum UserStatus {

    //注销
    LOGOUT(0, "注销"),
    //正常
    NORMAL(1, "正常"),
    //冻结
    FREEZE(2, "冻结"),
    ;

    @EnumValue//枚举值
    private final Integer status;//状态
    private final String description;//状态描述


    /**
     * 构造方法
     * @param status 状态
     * @param description 描述
     */
    UserStatus(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    /**
     * 等于方法
     * @param status 状态
     * @return 是否相等
     */
    public Boolean equalsVal(UserStatus status) {
        if (status == null) {
            return false;
        }
        return Objects.equals(this.status, status.status);
    }


}
