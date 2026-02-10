package com.itcast.myweb.utils;

import cn.hutool.core.util.RandomUtil;

public class Code {//生成验证码


    public static String createCode() {


        return RandomUtil.randomNumbers(6);

    }


}
