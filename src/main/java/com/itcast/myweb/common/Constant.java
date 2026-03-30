package com.itcast.myweb.common;

public class Constant {


    public static final String JWT_SECRET = "aXRjYXN0";// jwt密钥

    public static final Long JWT_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L;// jwt过期时间7天

    public static final String CODE_CACHE_KEY_PREFIX ="auth:code:";// 验证码缓存key前缀

    public static final Long CODE_EXPIRE_TIME = 5L;// 验证码过期时间5分钟

    public static final Integer LOGICAL_DELETED = 1;// 逻辑删除

    public static final Integer IS_NOT_DELETED = 0;// 非逻辑删除


    public static final Integer ACCOUNT_LOG_OFF = 0;//注销
    public static final Integer ACCOUNT_NORMAL = 1;//正常
    public static final Integer ACCOUNT_FREEZE = 0;//冻结



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
    public static final String DEFAULT_AVATAR_URL = "https://myweb-oss.oss-cn-beijing.aliyuncs.com/avatar/default.png";


    //令牌黑名单key
    public static final String TOKEN_BLACKLIST_KEY = "auth:blacklist:ids";

    //失效令牌key前缀
    public static final String TOKEN_INVALID_KEY_PREFIX = "auth:logout:user:";



    //分类缓存
    public static final String CATEGORY_CACHE_KEY = "cache:category";


    //返回热门商品数量
    public static final Integer HOT_ITEM_COUNT = 1000;
    //返回热门商品数量（前端展示）
    public static final Integer HOT_ITEM_COUNT_FRONT = 300;

    //根据用户偏好返回商品数量
    public static final Integer PREFERENCE_ITEM_COUNT = 200;
    //携带的热门商品数量
    public static final Integer MIXED_HOT_ITEM_COUNT = 200;



    public static final String HOT_ITEM_CACHE_KEY = "cache:hotItem";// 热门商品缓存key


    //热门商品缓存过期时间
    public static final Integer HOT_ITEM_CACHE_TTL = 10;// 热门商品缓存过期时间10分钟


    //验证码状态码
    public static final Integer CODE_EMPTY = 1;// 验证码为空
    public static final Integer CODE_ERROR = 3;// 验证码错误
    public static final Integer CODE_EXPIRE = 2;// 验证码过期或不存在
    public static final Integer CODE_SUCCESS = 0;// 验证码成功

    //验证码状态码描述
    public static final String MSG_EMPTY = "验证码为空";
    public static final String MSG_ERROR = "验证码错误";
    public static final String MSG_EXPIRE = "验证码过期或不存在";

    public static final String CATEGORY_PREFERENCE_CACHE_KEY = "cache:categoryPreference:";// 用户偏好分类缓存key

    public static final long CATEGORY_PREFERENCE_CACHE_TTL = 7;// 用户偏好分类缓存过期时间7天

    public static final long CATEGORY_CACHE_TTL = 7;// 分类缓存过期时间7天

    //用户昵称前缀
    public static final String NICK_NAME_PREFIX = "nick_";

    //用户账号前缀
    public static final String ACCOUNT_NAME_PREFIX = "user_";






}
