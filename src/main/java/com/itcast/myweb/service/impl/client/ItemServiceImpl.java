package com.itcast.myweb.service.impl.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.ItemStockInsufficientException;
import com.itcast.myweb.common.pojo.KeyFunc;
import com.itcast.myweb.common.pojo.OrderClazz;
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
import com.itcast.myweb.utils.CacheSolution;
import com.itcast.myweb.utils.OrderItemUtils;
import com.itcast.myweb.utils.TTLOffset;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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
     * 缓存解决方案
     */
    private final CacheSolution cacheSolution;


    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    @Override
    public List<CategoryVO> category() {

        //从缓存中获取分类列表

        //判断缓存是否存在
        List<String> categoryJsonList = stringRedisTemplate.opsForList().range(Constant.CATEGORY_CACHE_KEY, 0, -1);
        if (categoryJsonList != null && !categoryJsonList.isEmpty()) {
            //从缓存中解析分类列表
            return categoryJsonList.stream()
                    .map(json -> JSONUtil.toBean(json, CategoryVO.class))
                    .collect(Collectors.toList());
        }

        //不存在
        //查询分类列表
        List<Category> categoryList = categoryService.list();

        //转换为VO
        List<CategoryVO> categoryVOList = BeanUtil.copyToList(categoryList, CategoryVO.class);

        //缓存分类列表
        stringRedisTemplate.opsForList().leftPushAll(Constant.CATEGORY_CACHE_KEY,
                categoryVOList.stream()
                        .map(JSONUtil::toJsonStr)
                        .collect(Collectors.toList()));


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


        //-----------从缓存中获取热门商品列表

        //判断缓存是否存在
        String key = Constant.HOT_ITEM_CACHE_KEY_PREFIX + itemPageDTO.getCategoryId() + ":" + itemPageDTO.getPageNo();
        PageResult<ItemBaseVO> pageResultCache = getPageResultFromCache(key, itemPageDTO);
        if (pageResultCache != null) {
            return pageResultCache;
        }


        //--------------从数据库中查询热门商品列表

        //构建分页条件
        Page<ItemBase> page = new Page<>();
        page.setCurrent(itemPageDTO.getPageNo());
        page.setSize(itemPageDTO.getPageSize());
        List<OrderClazz> orderClazzList = itemPageDTO.getOrderClazzList();
        List<OrderItem> orderItems = OrderItemUtils.buildOrderItem(orderClazzList);
        page.addOrder(orderItems);

        //分页查询热门商品
        itemBaseService.lambdaQuery()
                .eq(itemPageDTO.getCategoryId() != null, ItemBase::getCategoryId, itemPageDTO.getCategoryId())
                .eq(ItemBase::getOnSale, Boolean.TRUE)
                .page(page);


        List<ItemBase> records = page.getRecords();
        //转为DTO
        List<ItemBaseVO> itemBaseVOList = BeanUtil.copyToList(records, ItemBaseVO.class);


        //组装结果
        PageResult<ItemBaseVO> pageResult = PageResult.<ItemBaseVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(itemBaseVOList)
                .build();


        //判断页数是否超过最大页数
        Long pages = pageResult.getPages();
        if (pages <= Constant.HOT_ITEM_CATEGORY_CACHE_PAGE) {

            //缓存热门商品ids
            setIdsToListCache(key, records, ItemBase::getId, Constant.CATEGORY_HOT_ITEM_IDS_CACHE_TTL, TimeUnit.MINUTES);

            //批量缓存spu详情
            multiSetStringCache(records, Constant.ITEM_SPU_CACHE_TTL, TimeUnit.MINUTES);

        }


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

        ItemBase itemBase = null;

        ItemBaseVO itemBaseVO = new ItemBaseVO();

        List<ItemSku> itemSkuList = new ArrayList<>();

        ItemDetailVO itemDetailVO = new ItemDetailVO();


        //查询缓存中spu详情
        String spuJson = stringRedisTemplate.opsForValue().get(Constant.ITEM_SPU_CACHE_KEY_PREFIX + id);
        if (spuJson != null) {
            //存在
            itemBase = JSONUtil.toBean(spuJson, ItemBase.class);
        } else {

            //不存在
            //查询数据库中spu详情
            itemBase = itemBaseService.lambdaQuery()
                    .eq(ItemBase::getId, id)
                    .eq(ItemBase::getOnSale, Boolean.TRUE)
                    .one();

            //判断spu详情是否为空
            if (itemBase == null) {
                cacheSolution.setNull(Constant.ITEM_SPU_CACHE_KEY_PREFIX + id);
                return null;
            }


            //缓存spu详情
            //设置过期时间
            //包含偏移量的过期时间，防止雪崩
            stringRedisTemplate.opsForValue().set(Constant.ITEM_SPU_CACHE_KEY_PREFIX + id,
                    JSONUtil.toJsonStr(itemBase),
                    TTLOffset.getRandomTTL(Constant.ITEM_SPU_CACHE_TTL), TimeUnit.MINUTES);


            // TODO 判断商品是否热门，决定缓存时间，引入关键字搜索频率作为参考
        }

        BeanUtil.copyProperties(itemBase, itemBaseVO);
        BeanUtil.copyProperties(itemBaseVO, itemDetailVO);


        //------------ 2.查询sku列表


        //查询sku列表缓存
        List<String> skuIdJsonList = stringRedisTemplate.opsForList().range(Constant.SPU_SKUS_CACHE_KEY_PREFIX + id, 0, -1);
        if (skuIdJsonList != null && !skuIdJsonList.isEmpty()) {//存在
            //从缓存中解析sku列表
            List<Long> skuIds = skuIdJsonList.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            //根据ids查询缓存中的sku详情
            List<Long> miss = new ArrayList<>();
            for (Long skuId : skuIds) {
                String skuJson = stringRedisTemplate.opsForValue().get(Constant.ITEM_SKU_CACHE_KEY_PREFIX + skuId);
                if (skuJson == null) {
                    miss.add(skuId);
                } else {
                    ItemSku itemSku = JSONUtil.toBean(skuJson, ItemSku.class);
                    itemSkuList.add(itemSku);
                }
            }

            //查询缺失的sku详情
            if (!miss.isEmpty()) {
                List<ItemSku> missList = itemSkuService.lambdaQuery()
                        .in(ItemSku::getId, miss)
                        .eq(ItemSku::getOnSale, Boolean.TRUE)
                        .list();
                itemSkuList.addAll(missList);
            }


        } else {//不存在
            //根据id查询sku列表
            itemSkuList = itemSkuService.lambdaQuery()
                    .eq(ItemSku::getBaseId, id)
                    .eq(ItemSku::getOnSale, Boolean.TRUE)
                    .list();

            //缓存skuIds
            setIdsToListCache(Constant.SPU_SKUS_CACHE_KEY_PREFIX + id, itemSkuList, ItemSku::getId, Constant.SPU_SKUS_CACHE_TTL, TimeUnit.MINUTES);

            //批量缓存sku
            multiSetStringCache(itemSkuList, Constant.ITEM_SKU_CACHE_TTL, TimeUnit.MINUTES);


        }


        itemDetailVO.setSkuVOList(BeanUtil.copyToList(itemSkuList, ItemSkuVO.class));

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


        //准备数据
        Long pageNo = itemPageDTO.getPageNo();
        Long pageSize = itemPageDTO.getPageSize();
        List<OrderItem> orderItems = OrderItemUtils.buildOrderItem(itemPageDTO.getOrderClazzList());
        String name = itemPageDTO.getName();


        List<ItemBaseVO> itemBaseVOList = null;

        //-------------0.查询缓存

        //缓存key包含所有查询条件，相同查询条件，标识一次查询
        String idsKey = Constant.ITEM_SPU_IDS_SEARCH_CACHE_KEY_PREFIX + name + ":" + pageNo;
        PageResult<ItemBaseVO> pageResultCache = getPageResultFromCache(idsKey, itemPageDTO);
        if (pageResultCache != null) {
            return pageResultCache;
        }


        //-------------1.一级缓存未命中，分页查询商品列表

        //构建分页条件
        Page<ItemBase> page = new Page<>(pageNo, pageSize);
        page.addOrder(orderItems);
        //查询商品列表
        itemBaseService.lambdaQuery()
                .like(name != null, ItemBase::getTitle, name)//根据名称模糊查询
                .eq(ItemBase::getOnSale, Boolean.TRUE)//上架商品
                .page(page);//分页查询

        //转为ItemBaseVOList
        List<ItemBase> records = page.getRecords();
        itemBaseVOList = BeanUtil.copyToList(records, ItemBaseVO.class);


        //-------------2.组合结果

        //返回ItemBaseVO
        PageResult<ItemBaseVO> pageResult = PageResult.<ItemBaseVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(itemBaseVOList)
                .build();

        //缓存spuids
        setIdsToListCache(idsKey, records, ItemBase::getId, Constant.ITEM_SPU_SEARCH_CACHE_TTL, TimeUnit.MINUTES);

        //建立二级缓存
        multiSetStringCache(records, Constant.ITEM_SPU_CACHE_TTL, TimeUnit.MINUTES);

        return pageResult;
    }


    /**
     * 扣减库存
     *
     * @param id  商品id
     * @param num 扣减数量
     */
    @Override
    public void deduckStock(Long id, Integer num) {

        //查询库存是否充足
        ItemSku itemSku = itemSkuService.getById(id);
        if (itemSku == null || itemSku.getStock() < num) {
            throw new ItemStockInsufficientException("库存不足");
        }


        //判断数据可用后立刻更新库存
        itemSkuService.lambdaUpdate()
                .set(ItemSku::getStock, itemSku.getStock() - num)
                .eq(ItemSku::getId, id)
                .update();

    }


    //缓存ids
    public <T> void setIdsToListCache(String idsKey, List<T> records, Function<T, Long> function, Long cacheTTL, TimeUnit timeUnit) {
        //缓存ids
        stringRedisTemplate.opsForList()
                .rightPushAll(idsKey, records.stream().map(function).map(Object::toString).collect(Collectors.toList()));
        //设置过期时间
        stringRedisTemplate.expire(idsKey, TTLOffset.getRandomTTL(cacheTTL), timeUnit);
    }


    //批量缓存对象为String格式
    public <T extends KeyFunc> void multiSetStringCache(List<T> list, Long cacheTTL, TimeUnit timeUnit) {
        //缓存热门spu列表
        Map<String, String> map = list.stream().map(
                item -> Map.entry(item.generateKey(), JSONUtil.toJsonStr(item))
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        stringRedisTemplate.opsForValue().multiSetIfAbsent(map);
        //设置过期时间
        Set<String> keySet = map.keySet();
        keySet.forEach(key -> stringRedisTemplate.expire(key, TTLOffset.getRandomTTL(cacheTTL), timeUnit));
    }


    //获取缓存中的PageResult
    private PageResult<ItemBaseVO> getPageResultFromCache(String idsKey, ItemPageDTO itemPageDTO) {
        //查询缓存中的商品id列表
        List<String> itemIds = stringRedisTemplate.opsForList().range(idsKey, 0, -1L);
        //保存不在缓存中的商品id
        List<Long> idlist = new ArrayList<>();
        if (itemIds != null && !itemIds.isEmpty()) {
            //缓存不为空，从缓存中解析商品列表
            List<ItemBase> itemBaseList = new ArrayList<>();
            List<Long> ids = itemIds.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            //循环处理ids
            for (Long itemId : ids) {
                String spuJson = stringRedisTemplate.opsForValue().get(Constant.ITEM_SPU_CACHE_KEY_PREFIX + itemId);
                if (spuJson != null) {//不为空，解析
                    itemBaseList.add(JSONUtil.toBean(spuJson, ItemBase.class));
                } else {//为空，保存商品id
                    idlist.add(itemId);
                }
            }

            if (!idlist.isEmpty()) {
                //批量查询商品详情
                List<ItemBase> rest = itemBaseService.lambdaQuery()
                        .in(ItemBase::getId, idlist)
                        .list();
                //合并商品列表
                itemBaseList.addAll(rest);
                //批量缓存商品详情
                multiSetStringCache(rest, Constant.ITEM_SPU_CACHE_TTL, TimeUnit.MINUTES);
            }

            //相关查询条件DB总记录数与页数
            Long categoryId = itemPageDTO.getCategoryId();
            Long count = itemBaseService.lambdaQuery()
                    .eq(itemPageDTO.getCategoryId() != null, ItemBase::getCategoryId, categoryId)
                    .eq(ItemBase::getOnSale, Boolean.TRUE)
                    .count();
            Long pageSize = itemPageDTO.getPageSize();
            Long pages = count / pageSize + (count % pageSize > 0 ? 1 : 0);


            return PageResult.<ItemBaseVO>builder()
                    .total(count)
                    .pages(pages)
                    .records(BeanUtil.copyToList(itemBaseList, ItemBaseVO.class))
                    .build();
        }
        //缓存为空，返回null
        return null;
    }


}
