package com.itcast.myweb.common;

public class Constant {


    public static final String JWT_SECRET = "aXRjYXN0";// jwt密钥

    public static final Long JWT_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L;// jwt过期时间7天

    public static final String CODE_CACHE_KEY_PREFIX ="auth:code:";// 验证码缓存key前缀

    public static final Long CODE_EXPIRE_TIME = 5L;// 验证码过期时间5分钟


    //用户状态
    public static final Integer ACCOUNT_LOG_OFF = 0;//注销
    public static final Integer ACCOUNT_NORMAL = 1;//正常
    public static final Integer ACCOUNT_FREEZE = 0;//冻结



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


    //热门商品缓存页数
    public static final Integer HOT_ITEM_CACHE_PAGE = 3;


    public static final String HOT_ITEM_CACHE_KEY = "cache:hotItem:";// 热门商品缓存key前缀


    //热门商品缓存过期时间，前3页
    public static final Long HOT_ITEM_CACHE_TTL = 10L;// 热门商品缓存过期时间10分钟

    //普通商品缓存过期时间，4页以后
    public static final Long NORMAL_ITEM_CACHE_TTL = 5L;// 普通商品缓存过期时间5分钟


    //验证码状态码
    public static final Integer CODE_EMPTY = 1;// 验证码为空
    public static final Integer CODE_ERROR = 3;// 验证码错误
    public static final Integer CODE_EXPIRE = 2;// 验证码过期或不存在
    public static final Integer CODE_SUCCESS = 0;// 验证码成功

    //验证码状态码描述
    public static final String MSG_EMPTY = "验证码为空";
    public static final String MSG_ERROR = "验证码错误";
    public static final String MSG_EXPIRE = "验证码过期或不存在";


    //用户昵称前缀
    public static final String NICK_NAME_PREFIX = "nick_";

    //用户账号前缀
    public static final String ACCOUNT_NAME_PREFIX = "user_";

    //商品分页大小
    public static final Long ITEM_PAGE_SIZE = 48L;

    //购物车分页大小
    public static final Long CART_PAGE_SIZE = 20L;

    //默认页码
    public static final Long DEFAULT_PAGE_NO = 1L;

    //sku缓存key前缀
    public static final String ITEM_SKU_CACHE_KEY_PREFIX = "item:sku:";


    //sku缓存过期时间
    public static final Long ITEM_SKU_CACHE_TTL = 5L;// 商品sku缓存过期时间5分钟


    //商品基础缓存key前缀
    public static final String ITEM_BASE_LIST_CACHE_KEY_PREFIX = "item:base:list:";

    //商品基础列表缓存key前缀
    public static final String ITEM_BASE_CACHE_KEY_PREFIX = "item:base:";

    //商品基础缓存过期时间
    public static final Long ITEM_BASE_CACHE_TTL = 15L;// 商品基础缓存过期时间15分钟


    //spu搜索key前缀
    public static final String ITEM_SPU_SEARCH_KEY_PREFIX = "item:spu:search:";

    //spu搜索缓存过期时间
    public static final Long ITEM_SPU_SEARCH_CACHE_TTL = 10L;// 商品spu搜索缓存过期时间10分钟

    //购物车最大数量
    public static final Integer CART_MAX_NUM = 48;

    //商品数量最大值
    public static final Integer ITEM_MAX_NUM = 48;




}
