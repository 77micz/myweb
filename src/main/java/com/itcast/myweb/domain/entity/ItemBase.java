package com.itcast.myweb.domain.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.pojo.KeyFunc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品基础信息表spu
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("item_base")
@ApiModel(value = "商品基础信息表spu")
public class ItemBase implements Serializable, KeyFunc {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商户id")
    private Long merchantId;

    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    @ApiModelProperty(value = "当前关联的sku_id")
    private Long currentSkuId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "主图")
    private String image;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "所属分类id")
    private Long categoryId;

    @ApiModelProperty(value = "发货地址id")
    private Long deliveryAddressId;

    @ApiModelProperty(value = "发货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "销量")
    private Integer sales;

    @ApiModelProperty(value = "最近销量")
    private Integer recentSales;

    @ApiModelProperty(value = "是否上架,(0=否,1=是)")
    private Boolean onSale;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @Override
    public String generateKey() {
        return Constant.ITEM_SPU_CACHE_KEY_PREFIX + id;
    }
}
