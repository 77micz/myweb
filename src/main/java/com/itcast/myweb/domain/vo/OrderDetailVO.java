package com.itcast.myweb.domain.vo;


import com.itcast.myweb.enums.OrderStatus;
import com.itcast.myweb.enums.PaymentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单详情视图
 */
@Data
public class OrderDetailVO {


    //-----------------------order

    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "收货人")
    private String recipient;

    @ApiModelProperty(value = "收货人手机号")
    private String phone;

    @ApiModelProperty(value = "订单地址id")
    private Long addressId;

    @ApiModelProperty(value = "订单状态，（0=待付款，1=待发货，2=待收货，3=已完成，4=已取消，5=退款/售后）")
    private OrderStatus status;

    @ApiModelProperty(value = "支付方式，（1=支付宝，2=微信，3=余额）")
    private PaymentType paymentType;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "发货时间")
    private LocalDateTime deliveryTime;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty(value = "取消时间")
    private LocalDateTime cancelTime;


    //-----------------------------------------orderDetail

    @ApiModelProperty(value = "订单明细id")
    private Long orderDetailId;

    @ApiModelProperty(value = "商品id")
    private Long skuId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "主图")
    private String image;

    @ApiModelProperty(value = "规格值组合，格式：属性键值:属性值值，用,分隔")
    private String specsVal;

    @ApiModelProperty(value = "商家id")
    private Long merchantId;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "运费")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "优惠")
    private BigDecimal preferential;

    @ApiModelProperty(value = "实付款")
    private BigDecimal actualPayment;


    //-----------------------------------------payOrder

    @ApiModelProperty(value = "支付单号")
    private Long payOrderNo;


}
