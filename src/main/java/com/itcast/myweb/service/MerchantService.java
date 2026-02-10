package com.itcast.myweb.service;


import com.itcast.myweb.DTO.LoginDTO;
import com.itcast.myweb.DTO.RegisterDTO;
import com.itcast.myweb.pojo.Result;
import org.springframework.stereotype.Service;




public interface MerchantService {// 商户服务


    // 获取验证码
    Result getCode(String contactPhone);

    // 商户注册
    Result register(RegisterDTO registerDTO);


    // 登陆
    Result login(LoginDTO loginDTO);
}
