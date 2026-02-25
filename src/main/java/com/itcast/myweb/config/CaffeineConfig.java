package com.itcast.myweb.config;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.itcast.myweb.DTO.PCBaseDTO;
import com.itcast.myweb.DTO.PCGoodsDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {//缓存配置类



    //pcBaseCache
    @Bean
    public Cache<String, PCBaseDTO> pcBaseCache(){
        return Caffeine.newBuilder()
                .initialCapacity(100)//初始化缓存大小
                .maximumSize(10000)//最大缓存数量
                .expireAfterWrite(60, TimeUnit.MINUTES)//缓存失效时间
                .build();
    }



    //pcGoodsCache
    @Bean
    public Cache<String, PCGoodsDTO> pcGoodsCache(){
        return Caffeine.newBuilder()
                .initialCapacity(100)//初始化缓存大小
                .maximumSize(10000)//最大缓存数量
                .expireAfterWrite(10, TimeUnit.MINUTES)//缓存失效时间
                .build();
    }









}
