package com.itcast.myweb.domain.vo;


import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.pojo.KeyFunc;
import lombok.Data;

import java.math.BigDecimal;

@Data
// 商品详情VO
public class ItemSkuVO {

    //商品id
    private Long id;

    //商品价格
    private BigDecimal price;

    //商品优惠价
    private BigDecimal specialPrice;

    //商品图片
    private String image;

    //商品规格组合名称
    private String specsVal;

    //商品库存
    private Integer stock;


}
