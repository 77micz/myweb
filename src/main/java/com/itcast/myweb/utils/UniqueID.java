package com.itcast.myweb.utils;


import com.itcast.myweb.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Component
@Slf4j
public class UniqueID {// 唯一id


    //起始时间为2025-01-01 00:00:00
    private static final Long START_TIMESTAMP = 1767196800L;// 起始时间戳


    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    //获取唯一id
    public Long getUniqueId(String keyPrefix) {

        //获取当前时间时间戳
        long currentTimestamp = getCurrentTimestamp();

        //差值
        long diff = currentTimestamp - START_TIMESTAMP;

        //获取当前日期，并转为yyyyMMdd
        LocalDate now = LocalDate.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));


        //使用redis中string值的自增长方法获取唯一id
        Long increment = stringRedisTemplate.opsForValue().increment(keyPrefix + Constant.UNIQUE_ID_KEY + date);


        //差值左移32位
        long leftShift = diff << Constant.UNIQUE_ID_BIT_LENGTH;


        return leftShift | increment;


    }


//    public static void main(String[] args) {
//
//        LocalDate start = LocalDate.of(2026, 1, 1);
//
//        //设置时区
//        ZoneId zoneId = ZoneId.systemDefault();
//
//        //转为ZonedDateTime
//        ZonedDateTime zonedDateTime = start.atStartOfDay(zoneId);
//
//        //获取时间戳
//        long timestamp = zonedDateTime.toInstant().toEpochMilli();
//
//        //以秒为单位
//        timestamp = timestamp / 1000;
//
//
//        System.out.println(timestamp);
//
//
//    }


    //获取当前时间时间戳
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }


}
