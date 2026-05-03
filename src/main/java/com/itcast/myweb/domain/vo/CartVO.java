package com.itcast.myweb.domain.vo;


import com.itcast.myweb.enums.ItemStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车视图对象
 */
@Data
public class CartVO {

    private Long id;// 购物车id

    private Long skuId;// 商品sku id

    private Long baseId;// 商品基础id

    private Integer num;// 商品数量

    private BigDecimal price;// 商品价格

    private String title;// 商品标题

    private String image;// 商品图片

    private String specsVal;// 商品规格值

    private String merchantName;// 商户名称


    //-------------------- 非表字段

    private BigDecimal newPrice;// 商品最新价格

    private ItemStatus status;// 购物车状态

    private Integer stock;// 商品库存




}
