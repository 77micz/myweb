package com.itcast.myweb.service.client;


import com.itcast.myweb.domain.dto.LoginDTO;

public interface AuthService {


    //获取验证码
    String getCode(String contactPhone);


    //登陆
    String login(LoginDTO loginDTO);


    //登出
    void logout(String token);
}
