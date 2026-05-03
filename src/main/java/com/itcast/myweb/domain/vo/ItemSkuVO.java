package com.itcast.myweb.domain.vo;



import lombok.Data;

import java.math.BigDecimal;

@Data
// 商品详情VO
public class ItemSkuVO {

    //商品id
    private Long id;

    //商品价格
    private BigDecimal price;

    //商品图片
    private String image;

    //商品规格组合名称
    private String specsVal;

    //商品标题
    private String title;

    //商家名称
    private String merchantName;

    //商品库存
    private Integer stock;






}
