package com.itcast.myweb.utils;


import cn.hutool.core.util.RandomUtil;


/**
 * 缓存过期时间偏移量
 */
public class TTLOffset {

    /**
     * 左偏移量
     */
    private static final Long left = 1L;

    /**
     * 右偏移量
     */
    private static final Long right = 4L;


    /**
     * 获取包含偏移量的ttl
     */
    public static Long getRandomTTL(Long cacheTTL) {

        //获取左右之间随机值
        Long offset = RandomUtil.randomLong(left, right);

        return cacheTTL + (cacheTTL * offset / 10L);
    }


}
