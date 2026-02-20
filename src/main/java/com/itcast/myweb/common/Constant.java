package com.itcast.myweb.common;

public class Constant {


    public static final String JWT_SECRET = "aXRjYXN0";// jwt密钥

    public static final long JWT_Expire_Time = 1000 * 60 * 60 * 2;// jwt过期时间2小时

    public static final String CODE_KEY ="login:code:";// 验证码key

    public static final Long CODE_TIME = 5L;// 验证码过期时间5分钟

    public static final Integer LOGICAL_DELETED = 1;// 逻辑删除

    public static final Integer IS_NOT_DELETED = 0;// 非逻辑删除




}
