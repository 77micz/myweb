package com.itcast.myweb.common;

public class Constant {


    public static final String JWT_SECRET = "aXRjYXN0";// jwt密钥

    public static final long JWT_Expire_Time = 1000 * 60 * 60 * 2;// jwt过期时间2小时

    public static final String CODE_KEY ="login:code:";// 验证码key

    public static final Long CODE_TIME = 5L;// 验证码过期时间5分钟

    public static final Integer LOGICAL_DELETED = 1;// 逻辑删除

    public static final Integer IS_NOT_DELETED = 0;// 非逻辑删除


    public static final String PC_BASE_CACHE_KEY = "cache:commodity:base:pc:";// PC基础商品缓存key

    public static final String PC_GOODS_CACHE_KEY = "cache:commodity:goods:pc:";// PC商品缓存key

    //null缓存ttl
    public static final Integer NULL_CACHE_KEY_TTL = 2;

    //null缓存key
    public static final String NULL_CACHE_KEY = "null";

    //null缓存value
    public static final String NULL_CACHE_VALUE = "null";


    //goods ttl
    public static final Integer GOODS_CACHE_KEY_TTL = 10;


    //唯一id key
    public static final String UNIQUE_ID_KEY = ":uniqueId:";


    //自增位数
    public static final Integer UNIQUE_ID_BIT_LENGTH = 32;

    //默认头像地址
    public static final String DEFAULT_AVATAR_URL = "https://tianmao-oss.oss-cn-beijing.aliyuncs.com/avatar/default.png";


    //令牌黑名单
    public static final String TOKEN_BLACKLIST_KEY = "token:blacklist:";






}
