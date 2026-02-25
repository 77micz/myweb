package com.itcast.myweb.controller.c;


import com.itcast.myweb.DTO.CodeDTO;
import com.itcast.myweb.DTO.LoginDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tianmao/c/auth")
@Slf4j
public class AuthController {// 认证控制类

    @Autowired
    private AuthService authService;


    /**
     * 获取验证码
     * @param codeDTO
     * @return result
     */
    @PostMapping("/code")
    public Result getCode(@RequestBody CodeDTO codeDTO){


        log.info("获取验证码，手机号:{}",codeDTO.getContactPhone());

        String code = authService.getCode(codeDTO.getContactPhone());

        log.info("获取验证码成功，验证码:{}",code);

        return Result.ok(code);
    }



    /**
     * 登陆
     * @param loginDTO
     * @return result
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){

        log.info("用户登陆，手机号:{}",loginDTO.getContactPhone());


        return Result.ok(authService.login(loginDTO));
    }








}
