package com.itcast.myweb.service.impl.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.ItemDoesntExistException;
import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.domain.dto.ItemPageDTO;
import com.itcast.myweb.domain.entity.Category;
import com.itcast.myweb.domain.entity.ItemBase;
import com.itcast.myweb.domain.entity.ItemSku;
import com.itcast.myweb.domain.vo.CategoryVO;
import com.itcast.myweb.domain.vo.ItemBaseVO;
import com.itcast.myweb.domain.vo.ItemDetailVO;
import com.itcast.myweb.domain.vo.ItemSkuVO;
import com.itcast.myweb.service.client.ItemService;
import com.itcast.myweb.service.common.ICategoryService;
import com.itcast.myweb.service.common.IItemBaseService;
import com.itcast.myweb.service.common.IItemSkuService;
import com.itcast.myweb.utils.TTLOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {


    /**
     * 分类服务
     */
    private final ICategoryService categoryService;


    /**
     * spu服务
     */
    private final IItemBaseService itemBaseService;


    /**
     * 缓存
     */
    private final StringRedisTemplate stringRedisTemplate;


    /**
     * sku服务
     */
    private final IItemSkuService itemSkuService;


    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    @Override
    public List<CategoryVO> category() {

        //从缓存中获取分类列表

        //判断缓存是否存在
        Set<String> categoryJsonSet = stringRedisTemplate.opsForSet().members(Constant.CATEGORY_CACHE_KEY);
        if (categoryJsonSet != null && !categoryJsonSet.isEmpty()) {
            //从缓存中解析分类列表
            return categoryJsonSet.stream()
                    .map(json -> BeanUtil.toBean(json, CategoryVO.class))
                    .collect(Collectors.toList());
        }

        //不存在
        //查询分类列表
        List<Category> categoryList = categoryService.list();

        //转换为VO
        List<CategoryVO> categoryVOList = BeanUtil.copyToList(categoryList, CategoryVO.class);

        //缓存分类列表
        stringRedisTemplate.opsForSet().add(Constant.CATEGORY_CACHE_KEY,
                categoryVOList.stream()
                        .map(JSONUtil::toJsonStr)
                        .toArray(String[]::new));

        return categoryVOList;
    }


    /**
     * 商品分页查询
     *
     * @param itemPageDTO 商品分页查询DTO
     * @return 商品分页查询DTO
     */
    @Override
    public PageResult<ItemBaseVO> pageByCategory(ItemPageDTO itemPageDTO) {

        //判断每页数量是否为空
        if (itemPageDTO.getPageSize() == null || itemPageDTO.getPageSize() <= 0) {
            //设置为默认值
            itemPageDTO.setPageSize(Constant.ITEM_PAGE_SIZE);
        }


        //-----------从缓存中获取热门商品列表

        //判断缓存是否存在
        String key = Constant.HOT_ITEM_CACHE_KEY + itemPageDTO.getCategoryId() + ":" + itemPageDTO.getPageNo();
        PageResult<ItemBaseVO> pageResultCache = getPageResultFromCache(key);
        if (pageResultCache != null) {
            return pageResultCache;
        }
//        Set<String> hotJsonSet = stringRedisTemplate.opsForSet().members(key);
//        if (hotJsonSet != null && !hotJsonSet.isEmpty()) {
//            //从缓存中解析热门商品列表
//            //转换为DTO
//            List<PageResult<ItemBaseVO>> pageResults = hotJsonSet.stream()
//                    .map(json ->
//                    {
//                        JSON jsonObject = JSONUtil.parse(json);
//                        return jsonObject.toBean(new TypeReference<PageResult<ItemBaseVO>>() {
//                        });
//                    })
//                    .collect(Collectors.toList());
//            return pageResults.get(0);
//        }


        //--------------从数据库中查询热门商品列表

        //构建分页条件
        Page<ItemBase> page = new Page<>();
        page.setCurrent(itemPageDTO.getPageNo());
        page.setSize(itemPageDTO.getPageSize());

        //分页查询热门商品
        itemBaseService.lambdaQuery()
                .eq(ItemBase::getCategoryId, itemPageDTO.getCategoryId())
                .orderByDesc(ItemBase::getSales)
                .page(page);


        List<ItemBase> records = page.getRecords();
        //转为DTO
        List<ItemBaseVO> itemBaseVOList = BeanUtil.copyToList(records, ItemBaseVO.class);


        Long ttl = Constant.HOT_ITEM_CACHE_TTL;
        //判断缓存时间
        if (itemPageDTO.getPageNo() <= Constant.HOT_ITEM_CACHE_PAGE) {
            ttl = Constant.NORMAL_ITEM_CACHE_TTL;
        }


        //组装结果
        PageResult<ItemBaseVO> pageResult = PageResult.<ItemBaseVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(itemBaseVOList)
                .build();

        //缓存热门商品列表
        stringRedisTemplate.opsForSet().add(Constant.HOT_ITEM_CACHE_KEY + itemPageDTO.getCategoryId() + ":" + itemPageDTO.getPageNo(),
                JSONUtil.toJsonStr(pageResult));
        //设置过期时间
        stringRedisTemplate.expire(Constant.HOT_ITEM_CACHE_KEY + itemPageDTO.getCategoryId() + ":" + itemPageDTO.getPageNo(), TTLOffset.getRandomTTL(ttl), TimeUnit.MINUTES);


        return pageResult;
    }


    /**
     * 查询商品详情
     *
     * @param id 商品id
     * @return 商品详情
     */
    @Override
    public ItemDetailVO detail(Long id) {


        //------------ 1.查询spu详情

        ItemBaseVO itemBaseVO = null;


//        Set<String> itemJsonSet = stringRedisTemplate.opsForSet().members(Constant.ITEM_BASE_CACHE_KEY_PREFIX + id);
//        if (itemJsonSet != null && !itemJsonSet.isEmpty()) {
//            //查询缓存中spu详情
//            itemBaseVO = itemJsonSet.stream()
//                    .map(json -> JSONUtil.toBean(json, ItemBaseVO.class))
//                    .collect(Collectors.toList()).get(0);
//        } else {
//
//            //不存在
//            //查询数据库中spu详情
//            ItemBase itemBase = itemBaseService.lambdaQuery()
//                    .eq(ItemBase::getId, id)
//                    .eq(ItemBase::getIsOnSale, Boolean.TRUE)
//                    .one();
//
//            //判断spu详情是否为空
//            if (itemBase == null) {
//                throw new ItemDoesntExistException("商品不存在");
//            }
//
//            //转为ItemBaseVO
//            itemBaseVO = BeanUtil.toBean(itemBase, ItemBaseVO.class);
//
//
//            //缓存spu详情
//            stringRedisTemplate.opsForSet().add(Constant.ITEM_BASE_CACHE_KEY_PREFIX + id,
//                    JSONUtil.toJsonStr(itemBaseVO));
//
//
//            //设置过期时间
//            //包含偏移量的过期时间，防止雪崩
//            // TODO 判断商品是否热门，决定缓存时间，引入关键字搜索频率作为参考
//            stringRedisTemplate.expire(Constant.ITEM_BASE_CACHE_KEY_PREFIX + id, TTLOffset.getRandomTTL(Constant.ITEM_BASE_CACHE_TTL), TimeUnit.MINUTES);
//        }

        //查询缓存中spu详情
        String itemJson = stringRedisTemplate.opsForValue().get(Constant.ITEM_BASE_CACHE_KEY_PREFIX + id);
        if (itemJson != null) {
            //存在
            JSONUtil.toBean(itemJson, ItemBaseVO.class);
        } else {

            //不存在
            //查询数据库中spu详情
            ItemBase itemBase = itemBaseService.lambdaQuery()
                    .eq(ItemBase::getId, id)
                    .eq(ItemBase::getIsOnSale, Boolean.TRUE)
                    .one();

            //判断spu详情是否为空
            if (itemBase == null) {
                throw new ItemDoesntExistException("商品不存在");
            }

            //转为ItemBaseVO
            itemBaseVO = BeanUtil.toBean(itemBase, ItemBaseVO.class);


            //缓存spu详情
            //设置过期时间
            //包含偏移量的过期时间，防止雪崩
            stringRedisTemplate.opsForValue().set(Constant.ITEM_BASE_CACHE_KEY_PREFIX + id,
                    JSONUtil.toJsonStr(itemBaseVO),
                    TTLOffset.getRandomTTL(Constant.ITEM_BASE_CACHE_TTL), TimeUnit.MINUTES);


            // TODO 判断商品是否热门，决定缓存时间，引入关键字搜索频率作为参考
        }


        //------------ 2.查询sku列表


        //查询sku列表缓存
        Set<String> skuJsonSet = stringRedisTemplate.opsForSet().members(Constant.ITEM_SKU_CACHE_KEY_PREFIX + id);
        if (skuJsonSet != null && !skuJsonSet.isEmpty()) {
            //从缓存中解析sku列表
            ItemDetailVO itemDetailVO = new ItemDetailVO();
            List<ItemSkuVO> itemSkuVOList = skuJsonSet.stream()
                    .map(json -> JSONUtil.toBean(json, ItemSkuVO.class))
                    .collect(Collectors.toList());
            BeanUtil.copyProperties(itemBaseVO, itemDetailVO);
            itemDetailVO.setSkuVOList(itemSkuVOList);

            return itemDetailVO;
        }


        //根据id查询sku列表
        List<ItemSku> itemSkuList = itemSkuService.lambdaQuery()
                .eq(ItemSku::getBaseId, id)
                .eq(ItemSku::getIsOnSale, Boolean.TRUE)
                .list();

        //转为ItemSkuVO
        List<ItemSkuVO> itemSkuVOList = BeanUtil.copyToList(itemSkuList, ItemSkuVO.class);

        //缓存sku列表
        Set<ZSetOperations.TypedTuple<String>> itemSkuVOZSet = itemSkuVOList.stream()
                .map(itemSkuVO -> {
                    return (ZSetOperations.TypedTuple<String>) new DefaultTypedTuple<String>(JSONUtil.toJsonStr(itemSkuVO), itemSkuVO.getId().doubleValue());
                })
                .collect(Collectors.toSet());
        stringRedisTemplate.opsForZSet().add(Constant.ITEM_SKU_CACHE_KEY_PREFIX + id, itemSkuVOZSet);
        //设置过期时间
        stringRedisTemplate.expire(Constant.ITEM_SKU_CACHE_KEY_PREFIX + id, TTLOffset.getRandomTTL(Constant.ITEM_SKU_CACHE_TTL), TimeUnit.MINUTES);


        //------------ 3.组合结果
        //返回ItemDetailVO
        ItemDetailVO itemDetailVO = new ItemDetailVO();
        BeanUtil.copyProperties(itemBaseVO, itemDetailVO);
        itemDetailVO.setSkuVOList(itemSkuVOList);
        return itemDetailVO;
    }


    /**
     * 搜索商品
     *
     * @param itemPageDTO 商品分页查询DTO
     * @return 商品分页查询DTO
     */
    @Override
    public PageResult<ItemBaseVO> pageSearch(ItemPageDTO itemPageDTO) {

        //判断每页数量是否为空
        if (itemPageDTO.getPageSize() == null || itemPageDTO.getPageSize() <= 0) {
            //设置为默认值
            itemPageDTO.setPageSize(Constant.ITEM_PAGE_SIZE);
        }


        List<ItemBaseVO> itemBaseVOList = null;

        //-------------0.查询缓存

        //缓存key包含所有查询条件，注意：顺序不能改变
        //相同顺序+相同查询条件，标识一次查询
        String key = Constant.ITEM_SPU_SEARCH_KEY_PREFIX + itemPageDTO.getCategoryId() + itemPageDTO.getName() + itemPageDTO.getPageNo();
        PageResult<ItemBaseVO> pageResultCache = getPageResultFromCache(key);
        if (pageResultCache != null) {
            return pageResultCache;
        }
//        Set<String> pageJsonSet = stringRedisTemplate.opsForSet().members(key);
//        if (pageJsonSet != null && !pageJsonSet.isEmpty()) {
//            //缓存不为空，从缓存中解析商品列表
//            List<PageResult<ItemBaseVO>> pageResults = pageJsonSet.stream()
//                    .map(json -> {
//                        JSON jsonObject = JSONUtil.parse(json);
//                        return jsonObject.toBean(new TypeReference<PageResult<ItemBaseVO>>() {
//                        });
//                    })
//                    .collect(Collectors.toList());
//            return pageResults.get(0);
//        }


        //-------------1.分页查询商品列表

        //构建分页条件
        Page<ItemBase> page = new Page<>(itemPageDTO.getPageNo(), itemPageDTO.getPageSize());
        //查询商品列表
        itemBaseService.lambdaQuery()
                .eq(itemPageDTO.getCategoryId() != null, ItemBase::getCategoryId, itemPageDTO.getCategoryId())//根据分类id查询
                .like(itemPageDTO.getName() != null, ItemBase::getTitle, itemPageDTO.getName())//根据名称模糊查询
                .eq(itemPageDTO.getIsOnSale() != null, ItemBase::getIsOnSale, itemPageDTO.getIsOnSale())//上架商品
                .orderBy(itemPageDTO.getSort() != null, itemPageDTO.getOrder().equals("asc"), ItemBase::getRecentSales)//按最近销售量排序
                .orderBy(itemPageDTO.getLastSort() != null, itemPageDTO.getLastOrder().equals("asc"), ItemBase::getId)//按id排序，兜底排序
                .page(page);//分页查询

        //转为ItemBaseVO
        itemBaseVOList = BeanUtil.copyToList(page.getRecords(), ItemBaseVO.class);


        //-------------2.组合结果

        //返回ItemBaseVO
        PageResult<ItemBaseVO> pageResult = PageResult.<ItemBaseVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(itemBaseVOList)
                .build();

        //缓存商品列表
        stringRedisTemplate.opsForSet().add(Constant.ITEM_SPU_SEARCH_KEY_PREFIX + ":" + itemPageDTO.getCategoryId() + ":" + itemPageDTO.getName() + ":" + itemPageDTO.getPageNo(),
                JSONUtil.toJsonStr(pageResult));
        //设置过期时间
        stringRedisTemplate.expire(Constant.ITEM_SPU_SEARCH_KEY_PREFIX + itemPageDTO.getCategoryId() + itemPageDTO.getName() + itemPageDTO.getPageNo(), TTLOffset.getRandomTTL(Constant.ITEM_SPU_SEARCH_CACHE_TTL), TimeUnit.MINUTES);

        return pageResult;
    }


    //获取缓存中的PageResult
    private PageResult<ItemBaseVO> getPageResultFromCache(String key) {
        //查询缓存中的商品列表
        Set<String> pageJsonSet = stringRedisTemplate.opsForSet().members(key);
        if (pageJsonSet != null && !pageJsonSet.isEmpty()) {
            //缓存不为空，从缓存中解析商品列表
            List<PageResult<ItemBaseVO>> pageResults = pageJsonSet.stream()
                    .map(json -> {
                        JSON jsonObject = JSONUtil.parse(json);
                        return jsonObject.toBean(new TypeReference<PageResult<ItemBaseVO>>() {
                        });
                    })
                    .collect(Collectors.toList());
            return pageResults.get(0);
        }
        //缓存为空，返回null
        return null;
    }


}
