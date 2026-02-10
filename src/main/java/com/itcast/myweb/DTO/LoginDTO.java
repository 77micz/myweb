package com.itcast.myweb.DTO;


import lombok.Data;

@Data
public class LoginDTO {// 登录DTO


    private String contactPhone;// 手机号

    private String code;// 验证码


}
