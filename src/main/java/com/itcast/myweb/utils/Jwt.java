package com.itcast.myweb.utils;

import com.itcast.myweb.common.Constant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;



public class Jwt {// JWT工具类




    // 创建JWT
    public static String createJWT(Map<String, Object> dataMap) {

        return Jwts.builder()
                .setClaims(dataMap)// 设置数据
                .signWith(SignatureAlgorithm.HS256, Constant.JWT_SECRET)// 设置密钥
                .setExpiration(new Date(System.currentTimeMillis() + Constant.JWT_Expire_Time))// 设置过期时间2小时
                .compact();

    }


    // 解析JWT
    public static Map<String, Object> parseJWT(String jwt) {

        return Jwts.parser()
                .setSigningKey(Constant.JWT_SECRET)// 密钥
                .parseClaimsJws(jwt)// 解析JWT
                .getBody();// 获取数据

    }



}
