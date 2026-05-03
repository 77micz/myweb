package com.itcast.myweb.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.models.auth.In;
import lombok.Getter;

/**
 * 行为类型
 */
@Getter
public enum BehaviorType {

    //浏览
    BROWSE(1, "浏览", 1),
    //收藏
    COLLECT(2, "收藏", 3),
    //加购
    ADD_TO_CART(3, "加购", 5),
    //下单
    ORDER(4, "下单", 10),
    //其他
    OTHER(5, "其他", 1),
    ;

    @EnumValue//枚举值
    private final Integer value;//行为类型
    private final String desc;//描述
    private final Integer weight;//权重


    /**
     * 构造方法
     *
     * @param value  行为类型
     * @param desc   描述
     * @param weight 权重
     */
    BehaviorType(Integer value, String desc, Integer weight) {
        this.value = value;
        this.desc = desc;
        this.weight = weight;
    }


}
