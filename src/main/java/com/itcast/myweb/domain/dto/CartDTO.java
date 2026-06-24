package com.itcast.myweb.domain.dto;


import lombok.Data;


/**
 * 购物车数据传输对象
 */
@Data
public class CartDTO {

    private Long id;// 购物车id

    private Long skuId;// SKU id

    private Integer num;// 商品数量


}
