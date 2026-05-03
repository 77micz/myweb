package com.itcast.myweb.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.itcast.myweb.enums.NotifyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("merchant")
@ApiModel(value = "店铺表")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商家名称")
    private String name;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人电话号码")
    private String contactPhone;

    @ApiModelProperty(value = "商户地址")
    private String address;

    @ApiModelProperty(value = "商户发货地址")
    private String deliveryAddress;

    @ApiModelProperty(value = "收款账户")
    private String account;

    @ApiModelProperty(value = "是否有效，（0=禁用，1=有效）")
    private Boolean isValid;

    @ApiModelProperty(value = "通知方式，（1=短信，2=站内信，3=都来）")
    private NotifyType notifyType;

    @ApiModelProperty(value = "接受通知的手机号")
    private String notifyPhone;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
