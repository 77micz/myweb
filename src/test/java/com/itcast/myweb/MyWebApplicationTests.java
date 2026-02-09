package com.itcast.myweb;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MyWebApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void testCreateJWT() {


        // 创建JWT
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "admin");
        String compact = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "aXRjYXN0")// 设置签名算法和密钥
                .addClaims(dataMap)// 添加数据
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))// 设置过期时间1小时
                .compact();// 生成JWT

        System.out.println(compact);
    }


    // 测试解析JWT
    @Test
    void testParseJWT() {

        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc3MDY2Mjg4NX0.vbu84hNApwBL0Q2oloZu9C-vXGdZDGIemVxuTKw3c_8";
        Map<String, Object> claims = Jwts.parser()
                .setSigningKey("aXRjYXN0")// 设置密钥
                .parseClaimsJws(jwt)// 解析JWT
                .getBody();
        System.out.println(claims);

    }




}
