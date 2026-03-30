package com.itcast.myweb.domain.dto;


import lombok.Data;

@Data
public class LoginDTO {// 登录DTO


    private String contactPhone;// 手机号

    private String code;// 验证码


}
