package com.itcast.myweb.service.impl;

import cn.hutool.json.JSONUtil;
import com.itcast.myweb.DTO.CategoryDTO;
import com.itcast.myweb.DTO.ItemDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.mapper.BehaviorMapper;
import com.itcast.myweb.mapper.CategoryMapper;
import com.itcast.myweb.mapper.ItemMapper;
import com.itcast.myweb.service.IndexService;
import com.itcast.myweb.utils.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class IndexServiceImpl implements IndexService {


    @Autowired
    private CategoryMapper categoryMapper;// 分类

    @Autowired
    private StringRedisTemplate stringRedisTemplate;// redis


    @Autowired
    private ItemMapper itemMapper;// 商品


    @Autowired
    private BehaviorMapper behaviorMapper;// 行为


    //获取分类列表
    @Override
    public List<CategoryDTO> listCategory() {

        // TODO 数据库缓存数据一致性问题

        //查询redis
        Set<String> members = stringRedisTemplate.opsForSet().members(Constant.CATEGORY_CACHE_KEY);

        //判断是否为空
        if (members != null && !members.isEmpty()) {
            //打印日志
            log.info("从redis中获取分类列表:{}", members);

            //将set转换为list
            return members.stream().map(item -> JSONUtil.toBean(item, CategoryDTO.class)).collect(Collectors.toList());


        }


        //调用mapper
        List<CategoryDTO> categoryDTOList = categoryMapper.selectList();

        //判断是否为空
        if (categoryDTOList != null && !categoryDTOList.isEmpty()) {
            //将list转换为set并缓存
            stringRedisTemplate.opsForSet().add(Constant.CATEGORY_CACHE_KEY, categoryDTOList.stream().map(item -> JSONUtil.toJsonStr(item)).collect(Collectors.toList()).toArray(new String[0]));
            //设置缓存过期时间
            stringRedisTemplate.expire(Constant.CATEGORY_CACHE_KEY, Constant.CATEGORY_CACHE_TTL, TimeUnit.DAYS);
        }


        return categoryDTOList;
    }


    //获取热销商品列表
    @Override
    public List<ItemDTO> getHotGoodsList(Integer count) {

        //查询redis
//        Set<String> members = stringRedisTemplate.opsForSet().members(Constant.HOT_ITEM_CACHE_KEY);

        Set<ZSetOperations.TypedTuple<String>> members = stringRedisTemplate.opsForZSet().reverseRangeWithScores(Constant.HOT_ITEM_CACHE_KEY, 0, count - 1);

        //判断是否为空
        if (members != null && !members.isEmpty()) {
            //打印日志
            log.info("从redis中获取热门商品列表:{}", members);

            //将set转换为list
            return members.stream().map(item -> JSONUtil.toBean(item.getValue(), ItemDTO.class)).collect(Collectors.toList());


        }


        //查询热门商品
        List<ItemDTO> itemDTOList = itemMapper.selectHotList(Constant.HOT_ITEM_COUNT);

        //判断是否为空
        if (itemDTOList == null || itemDTOList.isEmpty()) {
            return List.of();
        }

        //缓存热门商品
//        stringRedisTemplate.opsForSet().add(Constant.HOT_ITEM_CACHE_KEY,
//                itemDTOList
//                        .stream()
//                        .map(itemDTO -> JSONUtil.toJsonStr(itemDTO))
//                        .collect(Collectors.toList())
//                        .toArray(new String[0]));
        stringRedisTemplate.opsForZSet().add(Constant.HOT_ITEM_CACHE_KEY,
                itemDTOList.stream().map(
                        itemDTO -> new DefaultTypedTuple<>(JSONUtil.toJsonStr(itemDTO), itemDTO.getSales().doubleValue())
                ).collect(Collectors.toSet()));

        //设置缓存过期时间
        stringRedisTemplate.expire(Constant.HOT_ITEM_CACHE_KEY, Constant.HOT_ITEM_CACHE_TTL, TimeUnit.MINUTES);


        //返回集合中前count个商品
        return itemDTOList.stream().limit(count).collect(Collectors.toList());
    }


    /**
     * 根据用户偏好获取商品列表
     *
     * @param token 用户token
     * @return 商品列表
     */
    @Override
    public List<ItemDTO> getGoodsListByPreference(String token) {


        //解析token获取id
        Map<String, Object> dataMap = Jwt.parseJWT(token);
        //判断是否为空
        if (dataMap == null || dataMap.isEmpty()) {
            return List.of();
        }

        //获取用户id

        String userId = dataMap.get("id").toString();
        //判断是否为空
        if (userId == null || userId.isEmpty()) {
            return List.of();
        }
        //转为整数
        Integer userIdInt = Integer.parseInt(userId);


        //查询redis获取分类偏好
        Set<String> members = stringRedisTemplate.opsForSet().members(Constant.CATEGORY_PREFERENCE_CACHE_KEY + userIdInt);

        List<Long> categoryIdList = null;


        //判断是否为空
        if (members != null && !members.isEmpty()) {
            //打印日志
            log.info("从redis中获取用户偏好分类列表:{}", members);

            //将set转换为list
            categoryIdList = members.stream().map(Long::parseLong).collect(Collectors.toList());


        } else {
            //查询用户偏好排名前3的分类
            List<Map<String, Object>> categoryMapList = behaviorMapper.selectByUserId(userIdInt);


            //判断是否为空
            if (categoryMapList == null || categoryMapList.isEmpty()) {
                return List.of();
            }

            //流处理分类列表，获取category_id列表
            categoryIdList = categoryMapList.stream().map(item -> item.get("category_id") == null ? null : Long.parseLong(item.get("category_id").toString())).collect(Collectors.toList());
            //缓存分类id列表
            stringRedisTemplate.opsForSet().add(Constant.CATEGORY_PREFERENCE_CACHE_KEY + userIdInt, categoryIdList.stream().map(item -> item.toString()).collect(Collectors.toList()).toArray(new String[0]));
            //设置缓存过期时间
            stringRedisTemplate.expire(Constant.CATEGORY_PREFERENCE_CACHE_KEY + userIdInt, Constant.CATEGORY_PREFERENCE_CACHE_TTL, TimeUnit.DAYS);
        }


        //获取热门商品列表
        List<ItemDTO> hotItemDTOList = getHotGoodsList(Constant.MIXED_HOT_ITEM_COUNT);
        //判断是否为空
        if (hotItemDTOList == null || hotItemDTOList.isEmpty()) {
            return List.of();
        }

        //从热销商品列表获取用户偏好分类商品
        List<ItemDTO> itemDTOList = getPreferenceFromHotItem(hotItemDTOList, categoryIdList, Constant.PREFERENCE_ITEM_COUNT);


        //判断是否为空
        if (itemDTOList == null || itemDTOList.isEmpty()) {
            return hotItemDTOList;
        }


        //合并商品列表
        itemDTOList.addAll(hotItemDTOList);

        //根据id去除重复商品
        itemDTOList = itemDTOList.stream()
                .collect(Collectors.toMap(ItemDTO::getId, itemDTO -> itemDTO))
                .values()
                .stream()
                .collect(Collectors.toList());


        return itemDTOList;
    }

    private List<ItemDTO> getPreferenceFromHotItem(List<ItemDTO> hotItemDTOList, List<Long> categoryIdList, Integer itemCount) {
        //根据用户偏好分类获取热销商品列表获取itemCount个商品
        return hotItemDTOList.stream().
                filter(itemDTO ->
                        categoryIdList.contains(itemDTO.getCategoryId()))
                .limit(itemCount)
                .collect(Collectors.toList());
    }
}
