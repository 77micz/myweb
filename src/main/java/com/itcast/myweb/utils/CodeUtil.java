package com.itcast.myweb.utils;

import cn.hutool.core.util.RandomUtil;
import com.itcast.myweb.common.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


//验证码工具类
@Component
public class CodeUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;




    // 生成验证码
    public String createCode(String phone) {

        String code = RandomUtil.randomNumbers(6);
        // 保存验证码到redis
        stringRedisTemplate.opsForValue().set(Constant.CODE_KEY + phone,code, Constant.CODE_TIME, TimeUnit.MINUTES);
        return code;

    }


    // 校验验证码
    public Integer checkCode(String code,String phone) {

        // 校验验证码是否为空
        if (code == null || code.isEmpty()) {
            return 1;//验证码为空
        }
        // 从redis中获取验证码
        String redisCode = stringRedisTemplate.opsForValue().get(Constant.CODE_KEY + phone);


        // 校验redis中的验证码是否为空
        if(redisCode == null){
            return 2;//验证码过期或不存在
        }

        // 比较验证码
        if(code.equals(redisCode)){
            // 验证码正确，删除redis中的验证码
            stringRedisTemplate.delete(Constant.CODE_KEY + phone);
            return 0;//验证码正确
        }

        return 3;//验证码错误
    }


}
