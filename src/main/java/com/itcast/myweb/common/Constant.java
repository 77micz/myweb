package com.itcast.myweb.common;

public class Constant {


    // jwt密钥
    public static final String JWT_SECRET = "aXRjYXN0";

    // jwt过期时间7天
    public static final Long JWT_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L;

    // 验证码缓存key前缀
    public static final String CODE_CACHE_KEY_PREFIX = "auth:code:";

    // 验证码过期时间5分钟
    public static final Long CODE_EXPIRE_TIME = 5L;


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


    //sku缓存key前缀(格式: item:sku:商品id)
    public static final String ITEM_SKU_CACHE_KEY_PREFIX = "item:sku:";
    //sku缓存ttl 15分钟
    public static final Long ITEM_SKU_CACHE_TTL = 15L;


    //spu缓存key前缀(格式: item:spu:商品id)
    public static final String ITEM_SPU_CACHE_KEY_PREFIX = "item:spu:";
    //spu缓存ttl 60分钟
    public static final Long ITEM_SPU_CACHE_TTL = 60L;


    //热门商品缓存页数
    public static final Integer HOT_ITEM_CATEGORY_CACHE_PAGE = 5;
    // 热门商品缓存key前缀(格式: item:hot:分类id:页码)
    public static final String HOT_ITEM_CACHE_KEY_PREFIX = "item:hot:";
    //分类下热门商品ids缓存过期时间15分钟，前5页
    public static final Long CATEGORY_HOT_ITEM_IDS_CACHE_TTL = 15L;


    //spu关联的skuid集合缓存key前缀(格式: spu:skus:spuid)
    public static final String SPU_SKUS_CACHE_KEY_PREFIX = "spu:skus:";
    //spu关联的skuid集合缓存过期时间30分钟
    public static final Long SPU_SKUS_CACHE_TTL = 30L;


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
    public static final Long DEFAULT_ITEM_PAGE_SIZE = 48L;

    //购物车分页大小
    public static final Long DEFAULT_CART_PAGE_SIZE = 20L;

    //默认页码
    public static final Long DEFAULT_PAGE_NO = 1L;


    //spuids搜索key前缀(格式: item:spu:search:搜索关键字:页码)
    public static final String ITEM_SPU_IDS_SEARCH_CACHE_KEY_PREFIX = "item:spu:search:";
    //spuids搜索缓存过期时间5分钟
    public static final Long ITEM_SPU_SEARCH_CACHE_TTL = 5L;


    //缓存空值
    public static final String NULL_VAL = "null";
    //空值ttl 2分钟
    public static final Long NULL_VAL_TTL = 2L;


    //购物车最大数量
    public static final Integer CART_MAX_NUM = 48;

    //业务订单缓存key前缀
    public static final String ORDER_UNIQUE_ID_CACHE_KEY_PREFIX = "order:business";


    //订单分页大小
    public static final Long DEFAULT_ORDER_PAGE_SIZE = 20L;

    //查询订单状态-所有
    public static final Integer ORDER_STATUS_ALL = -1;

    //查询订单状态-已删除
    public static final Integer ORDER_STATUS_DELETED = -2;


    //生成支付单id的缓存key前缀
    public static final String PAY_ORDER_UNIQUE_ID_CACHE_KEY_PREFIX = "payOrder";


    //支付分布式锁key前缀
    public static final String PAY_LOCK_KEY = "pay:lock:";
    //支付分布式锁过期时间10秒
    public static final Long PAY_LOCK_TTL = 10L;

    //取消支付分布式锁key前缀
    public static final String PAY_CANCEL_LOCK_KEY = "pay:cancel:lock:";
    //取消支付分布式锁过期时间10秒
    public static final Long PAY_CANCEL_LOCK_TTL = 10L;

}
