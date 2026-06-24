package com.itcast.myweb.domain.vo;


import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.pojo.KeyFunc;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemBaseVO {

    // 商品id
    private Long id;

    // 商品分类id
    private Long categoryId;

    // 商品图片
    private String image;

    // 商品标题
    private String title;

    // 商品价格
    private BigDecimal price;

    // 商品折扣
    private BigDecimal discount;

    // 销售量
    private Integer sales;

    // 店铺名称
    private String merchantName;

    // 店铺id
    private Long merchantId;

    // 发货地址
    private String deliveryAddress;

}
