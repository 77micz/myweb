package com.itcast.myweb.domain.vo;


import lombok.Data;

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
    private Double price;

    // 销售量
    private Integer sales;

    // 店铺名称
    private String merchantName;

    // 店铺id
    private Long merchantId;

    // 发货地址
    private String deliveryAddress;

}
