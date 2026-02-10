package com.itcast.myweb.entity;


import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class Order {// 订单


    private Long id;// id

    private Long userId;// 用户id

    private Long merchantId;// 商户id

    private String receiverName;// 收货人名称

    private String receiverPhone;// 收货人手机号

    private Long addressId;// 地址id

    private Long goodsId;// 商品id

    private Integer goodsCount;// 商品数量

    private Integer status;// 订单状态,0=待付款，1=待发货，2=待收货，3=已完成，4=已取消

    private Double amount;//商品总价

    private Double realPay;//实际支付金额

    private Integer paymentMethod;//支付方式,1=微信支付，0=支付宝支付

    private LocalDateTime payTime;//支付时间

    private LocalDateTime deliverTime;//发货时间

    private LocalDateTime createTime;//创建时间

    private LocalDateTime updateTime;//更新时间



}
