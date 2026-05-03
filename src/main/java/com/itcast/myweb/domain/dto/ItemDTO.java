package com.itcast.myweb.domain.dto;

import lombok.Data;

@Data
public class ItemDTO {

    private Long id;// 商品id

    private Long categoryId;// 商品分类id

    private String image;// 商品图片

    private String title;// 商品标题

    private Double price;// 商品价格

    private Integer sales;// 销售量

    private Integer recentSales;// 最近销量

    private Long deliveryAddressId;// 商品发货地址id

    private String merchantName;// 商户名称

    private Long merchantId;// 商户id


}
