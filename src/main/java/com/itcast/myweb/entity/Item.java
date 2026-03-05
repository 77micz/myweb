package com.itcast.myweb.entity;


import lombok.Data;

@Data
public class Item {


    private Long id;

    private Long baseId;// 商品基础id

    private Long categoryId;// 商品分类id

    private String title;// 商品标题

    private String image;// 商品图片

    private Double price;// 商品价格

    private Integer sales;// 销售量

    private Integer recentSales;// 最近销售量

    private Integer isOnSale;// 是否上架,0:下架,1:上架



//----------- 表外字段

    private Long MerchantId;// 商户id



}
