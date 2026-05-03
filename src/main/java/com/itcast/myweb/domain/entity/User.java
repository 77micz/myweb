package com.itcast.myweb.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.itcast.myweb.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
@ApiModel(value = "用户表")
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键，用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "账户名")
    private String accountName;

    @ApiModelProperty(value = "性别,(1=男,2=女)")
    private String gender;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户默认收货地址id")
    private Long defaultAddressId;

    @ApiModelProperty(value = "用户昵称")
    private String nick;

    @ApiModelProperty(value = "头像")
    private String image;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "余额")
    private Integer balance;

    @ApiModelProperty(value = "付款账户")
    private String account;

    @ApiModelProperty(value = "账号状态：0-注销 1-正常 2-冻结")
    private UserStatus status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
