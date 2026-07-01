package com.itcast.myweb.domain.dto;


import com.itcast.myweb.enums.PaymentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 订单DTO
 */
@Data
public class OrderDTO {

    @ApiModelProperty(value = "支付方式，（1=支付宝，2=微信，3=余额）")
    private PaymentType paymentType;

    /**
     * 订单id
     */
    private Long orderId;


    /**
     * 收货地址ids
     */
//    private List<Long> addressIds;


    //---------------购物车购买

    //购物车ids
//    private List<Long> cartIds;

    /**
     * 关联购物车id与收货地址id对应关系
     */
    private Map<Long, Long> cartIdAddressIdMap;


    //---------------商品购买


    /**
     * skuId
     */
    private Long skuId;

    /**
     * 收货地址id
     */
    private Long addressId;


    /**
     * 数量
     */
    private Integer num;


}
