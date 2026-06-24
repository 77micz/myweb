package com.itcast.myweb.utils;


import com.itcast.myweb.common.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CacheSolution {


    private final StringRedisTemplate redisTemplate;


    // 缓存空值
    public void setNull(String key) {
        redisTemplate.opsForValue().set(key, Constant.NULL_VAL, Constant.NULL_VAL_TTL, TimeUnit.MINUTES);
    }


}
