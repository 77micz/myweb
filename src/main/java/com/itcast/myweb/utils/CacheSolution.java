package com.itcast.myweb.utils;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class CacheSolution {


    private StringRedisTemplate redisTemplate;

    public CacheSolution(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }



    // 缓存数据
    public void setCache(String key, String value){}







}
