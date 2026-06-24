package com.itcast.myweb.domain.dto;


import lombok.Data;

/**
 * 订单查询参数
 */
@Data
public class OrderSearchDTO {


    /**
     * 订单ID
     */
    private Long orderId;


    /**
     * 商品标题
     */
    private String title;


    /**
     * 店铺名称
     */
    private String merchantName;


}
