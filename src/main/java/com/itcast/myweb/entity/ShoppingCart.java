package com.itcast.myweb.entity;


import cn.hutool.log.Log;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShoppingCart {// 购物车


    private Long id;// 购物车ID

    private Long userId;// 用户ID

    private Long goodsId;// 商品ID

    private Integer quantity;// 商品数量

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
