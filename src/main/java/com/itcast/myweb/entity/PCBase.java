package com.itcast.myweb.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PCBase {// PC基础商品


    private Long id;// 商品id

    private Long MerchantId;// 商户id

    private String name;// 商品名称

    private String image;// 商品图片

    private Double basePrice;// 商品基础价格

    private Long categoryId;// 商品分类id

    private Integer isOnSale;// 是否上架,0:下架,1:上架

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
