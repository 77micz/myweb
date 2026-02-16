package com.itcast.myweb.DTO;


import lombok.Data;

@Data
public class PCBaseDTO {// PC基础商品DTO

    private Long id;// 商品id

    private String name;// 商品名称

    private String image;// 商品图片

    private Double basePrice;// 商品基础价格

    private Long categoryId;// 商品分类id

    private Integer isOnSale;// 是否上架

    private Long merchantId;// 商户id

}
