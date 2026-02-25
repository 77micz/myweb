package com.itcast.myweb.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.github.benmanes.caffeine.cache.Cache;
import com.itcast.myweb.DTO.PCGoodsDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.mapper.GoodsSpecValueMapper;
import com.itcast.myweb.mapper.PCGoodsMapper;
import com.itcast.myweb.mapper.SpecValueMapper;
import com.itcast.myweb.service.PCGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Service
public class PCGoodsServiceImpl implements PCGoodsService {


    @Autowired
    private PCGoodsMapper pcGoodsMapper;


    @Autowired
    private GoodsSpecValueMapper goodsSpecValueMapper;


    @Autowired
    private SpecValueMapper specValueMapper;


    @Autowired
    private Cache<String, PCGoodsDTO> pcGoodsCache;// 商品缓存

    @Autowired
    private StringRedisTemplate stringRedisTemplate;// redis


    /**
     * 添加商品
     */

    @Override
    public void addCommodity(PCGoodsDTO pcGoodsDTO) {

        pcGoodsMapper.addGoods(pcGoodsDTO);

    }

    /**
     * 查询商品
     */
    @Override
    public PCGoodsDTO queryCommodity(Long id) {


        //缓存key
        String key = Constant.PC_GOODS_CACHE_KEY + id;


        //查询redis缓存
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        //判断是否为空
        if (!entries.isEmpty()) {
            //判断是否有null键
            if (entries.containsKey(Constant.NULL_CACHE_KEY)) {
                //缓存穿透
                return null;
            }

            //转为PCGoodsDTO
            PCGoodsDTO pcGoodsDTO = new PCGoodsDTO();
            CopyOptions copyOptions = CopyOptions.create()
                    .setIgnoreError(true)
                    .setIgnoreNullValue(true);
            BeanUtil.fillBeanWithMap(entries, pcGoodsDTO, copyOptions);
            //返回结果
            return pcGoodsDTO;
        }


        //查询本地缓存
        PCGoodsDTO pcGoodsDTO = pcGoodsCache.get(key, tempKey -> pcGoodsMapper.queryGoods(id, Constant.IS_NOT_DELETED));

        //判断是否为空
        if (pcGoodsDTO == null) {
            //处理缓存穿透
            //添加null缓存
            stringRedisTemplate.opsForHash().put(key, Constant.NULL_CACHE_KEY, Constant.NULL_CACHE_VALUE);
            stringRedisTemplate.opsForHash().expiration(key, Duration.ofMinutes(Constant.NULL_CACHE_KEY_TTL));
        }

        //添加缓存
        stringRedisTemplate.opsForHash().putAll(key, BeanUtil.beanToMap(pcGoodsDTO));
        stringRedisTemplate.opsForHash().expiration(key, Duration.ofMinutes(Constant.GOODS_CACHE_KEY_TTL));// 缓存有效期

        return pcGoodsDTO;

    }


    /**
     * 根据base_id查询商品
     */


    @Override
    public List<PCGoodsDTO> listCommodity(Long baseId) {

        //查询
        return pcGoodsMapper.listGoods(baseId);

    }


    /**
     * 修改商品
     */
    @Override
    public void updateCommodity(PCGoodsDTO pcGoodsDTO) {

        pcGoodsMapper.updateGoods(pcGoodsDTO);

        //使缓存失效
        stringRedisTemplate.delete(Constant.PC_GOODS_CACHE_KEY + pcGoodsDTO.getId());
        pcGoodsCache.invalidate(Constant.PC_GOODS_CACHE_KEY + pcGoodsDTO.getId());

    }


    /**
     * 删除商品
     */
    //软删除
    @Override
    public void delCommodity(Long id) {


        //获取所有商品规格值id
        List<Long> specValueIds = goodsSpecValueMapper.listSpecValueIds(id);

        //设置goods_spec_value为删除状态
        goodsSpecValueMapper.softDelGoodsSpecValue(id, Constant.LOGICAL_DELETED);

        //设置spec_value为删除状态
        //批量删除
        specValueMapper.batchSoftDelSpecValue(specValueIds);

        //设置goods为删除状态
        pcGoodsMapper.softDelGoods(id, Constant.LOGICAL_DELETED);


    }


    //恢复商品
    @Override
    public void restoreCommodity(Long id) {

    }


}
