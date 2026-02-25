package com.itcast.myweb.controller.entity;


import cn.hutool.core.util.PhoneUtil;
import com.itcast.myweb.DTO.CodeDTO;
import com.itcast.myweb.DTO.LoginDTO;
import com.itcast.myweb.DTO.RegisterDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tianmao/entity/merchant")
@Slf4j
public class MerchantController {//商户控制类


    @Autowired
    private MerchantService merchantService;//商户service


    //注册
    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){

        //过滤格式有误的手机号
        boolean mobile = PhoneUtil.isMobile(registerDTO.getContactPhone());
        if (!mobile){
            return Result.error("联系人手机号格式有误");
        }

        boolean notify = PhoneUtil.isMobile(registerDTO.getNotifyPhone());
        if (!notify){
            return Result.error("通知手机号格式有误");
        }

        //调用service方法
        return merchantService.register(registerDTO);

    }



    //获取验证码
    @PostMapping("/code")
    public Result getCode(@RequestBody CodeDTO codeDTO){
        //调用生成验证码的方法
        return merchantService.getCode(codeDTO.getContactPhone());
    }



    //登陆
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        //调用登陆方法
        return  merchantService.login(loginDTO);
    }







}
