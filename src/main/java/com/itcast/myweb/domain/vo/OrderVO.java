package com.itcast.myweb.domain.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.itcast.myweb.enums.OrderStatus;
import com.itcast.myweb.enums.PaymentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单视图
 */
@Data
public class OrderVO {


    @ApiModelProperty(value = "明细id")
    private Long id;

    @ApiModelProperty(value = "订单状态，（0=待付款，1=待发货，2=待收货，3=已完成，4=已取消，5=退款/售后）")
    private OrderStatus status;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

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

    @ApiModelProperty(value = "实付款")
    private BigDecimal actualPayment;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
