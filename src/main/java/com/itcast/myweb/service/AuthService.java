package com.itcast.myweb.service;


import com.itcast.myweb.DTO.LoginDTO;

public interface AuthService {


    //获取验证码
    String getCode(String contactPhone);


    //登陆
    String login(LoginDTO loginDTO);
}
