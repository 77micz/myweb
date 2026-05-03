package com.itcast.myweb.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.itcast.myweb.enums.OrderStatus;
import com.itcast.myweb.enums.PaymentType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_order")
@ApiModel(value = "订单表")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单号，主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)//全局唯一id
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "店铺id")
    private Long merchantId;

    @ApiModelProperty(value = "订单地址id")
    private Long addressId;

    @ApiModelProperty(value = "订单状态，（0=待付款，1=待发货，2=待收货，3=已完成，4=已取消，5=退款/售后）")
    private OrderStatus status;

    @ApiModelProperty(value = "总价")
    private BigDecimal totalFee;

    @ApiModelProperty(value = "优惠")
    private BigDecimal discount;

    @ApiModelProperty(value = "实付款")
    private BigDecimal realPay;

    @ApiModelProperty(value = "运费，包含在总价里")
    private BigDecimal shippingFee;

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

    @ApiModelProperty(value = "是否已删除，0-未删除，1-已删除")
    private Boolean deleted;

    @ApiModelProperty(value = "创建/下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
