package com.itcast.myweb.DTO;


import lombok.Data;

@Data
public class PCGoodsDTO {// PC商品DTO


    private Long id;// 商品id

    private Long baseId;// 基础商品id

    private Long goodsCode;// 商品编码

    private Long merchantId;// 商户id

    private Double price;// 价格

    private Integer stock;// 库存

    private Integer isDeleted;// 是否删除,0-否,1-是




}
