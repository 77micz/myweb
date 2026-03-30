package com.itcast.myweb.task;


import cn.hutool.json.JSONUtil;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.domain.dto.ItemDTO;
import com.itcast.myweb.service.common.IItemBaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// 重置热门商品任务
@Component
@Slf4j
@RequiredArgsConstructor
public class ResetHotItem {


//    private ItemService itemService;

    // 商品服务
    private final IItemBaseService itemService;



    private final StringRedisTemplate redisTemplate;

    /**
     * 重置热门商品
     */





    //每30天重置一次
    @Scheduled(fixedDelay = 30 * 24 * 60 * 60 * 1000L)
    public void resetRecentSales(){

        try {

            log.info("开始重置热门商品");
            //更新数据库
            List<ItemDTO> itemDTOList = itemService.updateDB();

            //判断是否为空
            if (itemDTOList == null || itemDTOList.isEmpty()) {
                log.info("热门商品列表为空,无需缓存");
                return;
            }


            //临时key
            String tempKey = Constant.HOT_ITEM_CACHE_KEY + ":temp";


            //将热门商品缓存到redis
            redisTemplate.opsForZSet().add(Constant.HOT_ITEM_CACHE_KEY,
                    itemDTOList.stream().map(
                            itemDTO -> new DefaultTypedTuple<>(JSONUtil.toJsonStr(itemDTO), itemDTO.getSales().doubleValue())
                    ).collect(Collectors.toSet()));

            //使缓存失效
            redisTemplate.delete(Constant.HOT_ITEM_CACHE_KEY);
            log.info("热门商品缓存已失效");

             //将临时key重命名为正式key
             redisTemplate.rename(tempKey,Constant.HOT_ITEM_CACHE_KEY);

            //设置缓存过期时间
            redisTemplate.expire(Constant.HOT_ITEM_CACHE_KEY, Constant.HOT_ITEM_CACHE_TTL, TimeUnit.MINUTES);

            log.info("热门商品缓存已更新");

        } catch (Exception e) {
            log.error("重置热门商品失败",e);
            throw new RuntimeException(e);
        }


    }
}
