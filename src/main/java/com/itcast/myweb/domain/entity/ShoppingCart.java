package com.itcast.myweb.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shopping_cart")
@ApiModel(value = "购物车表")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "商品id")
    private Long skuId;

    @ApiModelProperty(value = "商品基础id")
    private Long baseId;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "优惠价")
    private BigDecimal specialPrice;

    @ApiModelProperty(value = "主图")
    private String image;

    @ApiModelProperty(value = "规格值组合，格式：属性键:属性值，用,分隔")
    private String specsVal;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "店铺id")
    private Long merchantId;

    @ApiModelProperty(value = "店铺名称")
    private String merchantName;

    @TableLogic//逻辑删除
    @ApiModelProperty(value = "是否已删除，0-未删除，1-已删除")
    private Boolean deleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
