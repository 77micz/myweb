package com.itcast.myweb.handler;


import com.itcast.myweb.exception.*;
import com.itcast.myweb.pojo.Result;
import io.jsonwebtoken.*;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {// 异常处理


    //处理jwt异常
    @org.springframework.web.bind.annotation.ExceptionHandler(JwtException.class)
    public Result error(JwtException e) {
        if (e instanceof ExpiredJwtException) {
            return Result.error(401, "登录已过期，请重新登录");
        } else if (e instanceof SignatureException) {
            return Result.error(401, "令牌签名无效，拒绝访问");
        } else if (e instanceof MalformedJwtException) {
            return Result.error(401, "令牌格式错误");
        } else if (e instanceof UnsupportedJwtException) {
            return Result.error(401, "不支持的令牌类型");
        } else if (e instanceof PrematureJwtException) {
            return Result.error(401, "令牌暂未生效");
        } else if (e instanceof InvalidClaimException) {
            return Result.error(401, "令牌验证失败");
        } else {
            // 兜底捕获所有 JWT 异常
            return Result.error(401, "令牌解析失败：" + e.getMessage());
        }
    }


    //处理验证码异常
    @org.springframework.web.bind.annotation.ExceptionHandler(CodeException.class)
    public Result error(CodeException e) {
        if (e instanceof CodeExpiredException) {
            return Result.error(0, "验证码已过期");
        } else if (e instanceof NullCodeException) {
            return Result.error(0, "验证码不能为空");
        } else if (e instanceof IncorrectCodeException) {
            return Result.error(0, "验证码错误");
        } else {
            return Result.error(0, "验证码异常：" + e.getMessage());
        }
    }


    //处理手机号异常
    @org.springframework.web.bind.annotation.ExceptionHandler(PhoneException.class)
    public Result error(PhoneException e) {
        if (e instanceof PhoneFormatException) {
            return Result.error(0, "手机号格式有误");
        } else if (e instanceof NullPhoneException) {
            return Result.error(0, "手机号不能为空");
        } else {
            return Result.error(0, "手机号异常：" + e.getMessage());
        }
    }


    // 处理所有异常
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error("服务器错误");
    }


}
