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
 * 商品表sku
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("item_sku")
@ApiModel(value = "商品表sku")
public class ItemSku implements Serializable, KeyFunc {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联的基础商品id")
    private Long baseId;

    @ApiModelProperty(value = "商品编码")
    private Long skuCode;

    @ApiModelProperty(value = "所属分类id")
    private Long categoryId;

    @ApiModelProperty(value = "规格组合，格式：属性键id:属性值id，用,分隔")
    private String specsId;

    @ApiModelProperty(value = "规格组合，格式：属性键名称:属性值名称，用,分隔")
    private String specsVal;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "商家名称")
    private String merchantName;

    @ApiModelProperty(value = "商家id")
    private Long merchantId;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "优惠价")
    private BigDecimal specialPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "是否上架,(0=否，1=是)")
    private Boolean onSale;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    @Override
    public String generateKey() {
        return Constant.ITEM_SKU_CACHE_KEY_PREFIX + id;
    }
}
