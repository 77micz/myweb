package com.itcast.myweb.domain.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单详情DTO
 */
@Data
public class OrderDetailDTO {


    @ApiModelProperty(value = "商品id")
    private Long skuId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "主图")
    private String image;

    @ApiModelProperty(value = "规格值组合，格式：属性键值:属性值值，用,分隔")
    private String specsVal;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "商家id")
    private Long merchantId;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "优惠省的钱")
    private BigDecimal discountMoney;

    @ApiModelProperty(value = "优惠后价格")
    private BigDecimal afterDiscount;

    @ApiModelProperty(value = "数量")
    private Integer num;


}
