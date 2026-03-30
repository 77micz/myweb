package com.itcast.myweb.controller.client;



import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.domain.dto.CodeDTO;
import com.itcast.myweb.domain.dto.LoginDTO;
import com.itcast.myweb.service.client.AuthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myweb/c/auth")
@Slf4j
@Api(tags = "认证服务")
@RequiredArgsConstructor
public class AuthController {// 认证控制类


    // 服务
    private final AuthService authService;


    /**
     * 获取验证码
     * @param codeDTO
     * @return result
     */
    @PostMapping("/code")
    @ApiOperation(value = "获取验证码")
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
    @ApiOperation(value = "登陆")
    public Result login(@RequestBody LoginDTO loginDTO){

        log.info("用户登陆，手机号:{}",loginDTO.getContactPhone());


        return Result.ok(authService.login(loginDTO));
    }


    /**
     * 登出
     * @param token
     * @return result
     */
    @PostMapping("/logout")
    @ApiOperation(value = "登出")
    public Result logout(@RequestHeader("Authorization") String token){

        log.info("用户登出，token:{}",token);


        authService.logout(token);

        return Result.ok("退出登陆成功");
    }








}
