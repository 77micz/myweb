package com.itcast.myweb.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.itcast.myweb.enums.BehaviorType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户行为表
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_behavior")
@ApiModel(value = "用户行为表")
public class UserBehavior implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联用户id")
    private Long userId;

    @ApiModelProperty(value = "关联商品id")
    private Long itemId;

    @ApiModelProperty(value = "关联分类id")
    private Long categoryId;

    @ApiModelProperty(value = "用户行为类型,1:浏览,权重1 2:收藏,权重3 3:加购,权重5 4:下单,权重10 其他权重1")
    private BehaviorType behaviorType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


}
