package com.itcast.myweb.service.impl;

import com.itcast.myweb.DTO.PCGoodsDTO;
import com.itcast.myweb.mapper.GoodsSpecValueMapper;
import com.itcast.myweb.mapper.PCGoodsMapper;
import com.itcast.myweb.mapper.SpecValueMapper;
import com.itcast.myweb.service.PCGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PCGoodsServiceImpl implements PCGoodsService {


    @Autowired
    private PCGoodsMapper pcGoodsMapper;


    @Autowired
    private GoodsSpecValueMapper goodsSpecValueMapper;


    @Autowired
    private SpecValueMapper specValueMapper;



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

        return pcGoodsMapper.queryGoods(id);

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
        goodsSpecValueMapper.softDelGoodsSpecValue(id);

        //设置spec_value为删除状态
        //批量删除
        specValueMapper.batchSoftDelSpecValue(specValueIds);

        //设置goods为删除状态
        pcGoodsMapper.softDelGoods(id);



    }


}
