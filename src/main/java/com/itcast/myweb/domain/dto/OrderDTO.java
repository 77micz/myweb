package com.itcast.myweb.domain.dto;


import com.itcast.myweb.enums.PaymentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单DTO
 */
@Data
public class OrderDTO {

    @ApiModelProperty(value = "支付方式，（1=支付宝，2=微信，3=余额）")
    private PaymentType paymentType;


    /**
     * 收货地址id
     */
    private Long addressId;


    //---------------购物车购买

    //购物车ids
    private List<Long> cartIds;


    //---------------商品购买


    /**
     * skuId
     */
    private Long skuId;


    /**
     * 数量
     */
    private Integer num;


}
