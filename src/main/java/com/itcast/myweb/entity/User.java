package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {// 用户


    private Long id;// id

    private String name;// 用户名

    private String password;// 密码

    private String gender;// 性别

    private String phone;// 手机号

    private Long defaultAddressId;// 默认地址id

    private String nickName;// 昵称

    private String image;// 头像

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
