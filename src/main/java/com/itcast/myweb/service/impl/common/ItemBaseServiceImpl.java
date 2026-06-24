package com.itcast.myweb.service.impl.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.pojo.HotSelect;
import com.itcast.myweb.domain.dto.ItemDTO;
import com.itcast.myweb.domain.entity.ItemBase;
import com.itcast.myweb.mapper.ItemBaseMapper;
import com.itcast.myweb.service.common.IItemBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品基础信息表spu 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
@RequiredArgsConstructor
public class ItemBaseServiceImpl extends ServiceImpl<ItemBaseMapper, ItemBase> implements IItemBaseService, HotSelect<ItemDTO> {

    // redis
    private final StringRedisTemplate redisTemplate;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ItemDTO> updateDB() {
        // 重置热门商品的销售量为0
        resetRecentSales();

        //查询数据库以总销售量加载热门商品缓存
        return selectBySales();
    }

    private List<ItemDTO> selectBySales() {
        //获取热门商品
        List<ItemBase> itemBaseList = lambdaQuery()
                .eq(ItemBase::getOnSale, true)
                .orderByDesc(ItemBase::getSales)
                .last("limit " + Constant.HOT_ITEM_COUNT)
                .list();

        //转换为DTO
        return BeanUtil.copyToList(itemBaseList, ItemDTO.class);

    }


    // 重置热门商品
    private void resetRecentSales() {

        // 重置近期销售数量
        lambdaUpdate()
                .set(ItemBase::getRecentSales, 0)
                .orderByDesc(ItemBase::getRecentSales, ItemBase::getSales)
                .last("limit " + Constant.HOT_ITEM_COUNT)
                .update();
    }


    // 查询热门商品
    @Override
    public List<ItemDTO> selectHotList(Integer count) {

        // 查询缓存
        Set<String> itemDTOList = redisTemplate.opsForZSet().range(Constant.HOT_ITEM_CACHE_KEY_PREFIX, 0, count - 1);


        //判断缓存是否为空
        if (itemDTOList != null && !itemDTOList.isEmpty()) {
            //转换为DTO
            return itemDTOList.stream().map(itemDTO -> JSONUtil.toBean(itemDTO, ItemDTO.class)).collect(Collectors.toList());
        }


        //缓存为空，查询热销商品
        List<ItemBase> itemBaseList = lambdaQuery()
                .eq(ItemBase::getOnSale, true)
                .orderByDesc(ItemBase::getRecentSales)
                .last("limit " + count)
                .list();


        //转换为DTO
        List<ItemDTO> itemDTOS = BeanUtil.copyToList(itemBaseList, ItemDTO.class);

        //缓存
        redisTemplate.opsForZSet().add(Constant.HOT_ITEM_CACHE_KEY_PREFIX, itemDTOS.stream()
                .map(itemDTO -> new DefaultTypedTuple<>(
                        JSONUtil.toJsonStr(itemDTO), itemDTO.getRecentSales().doubleValue()
                ))
                .collect(Collectors.toSet()));

        return itemDTOS;

    }
}
