package com.itcast.myweb.entity;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {// 用户


    private Long id;// id

    private String name;// 用户名

    private String password;// 密码

    private String gender;// 性别

    private String phone;// 手机号

    private Long defaultAddressId;// 默认地址id

    private String nickName;// 昵称

    private String image;// 头像

    private String email;// 邮箱

    private Integer isDeleted;// 逻辑删除,0-未删除,1-已删除

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
