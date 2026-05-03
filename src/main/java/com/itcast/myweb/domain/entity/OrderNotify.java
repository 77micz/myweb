package com.itcast.myweb.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.itcast.myweb.enums.NotifyStatus;
import com.itcast.myweb.enums.NotifyType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单消息表
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_notify")
@ApiModel(value = "订单消息表")
public class OrderNotify implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联订单id")
    private Long orderId;

    @ApiModelProperty(value = "关联店铺id")
    private Long merchantId;

    @ApiModelProperty(value = "通知状态，（0=未发送，1=已发送，2=发送失败）")
    private NotifyStatus status;

    @ApiModelProperty(value = "通知内容")
    private String content;

    @ApiModelProperty(value = "通知方式，（1=短信，2=站内信，3=都发）")
    private NotifyType notifyType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发送时间")
    private LocalDateTime sendTime;


}
