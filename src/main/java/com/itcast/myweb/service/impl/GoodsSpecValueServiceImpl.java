package com.itcast.myweb.service.impl;

import com.itcast.myweb.DTO.GoodsSpecValueDTO;
import com.itcast.myweb.mapper.GoodsSpecValueMapper;
import com.itcast.myweb.service.GoodsSpecValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GoodsSpecValueServiceImpl implements GoodsSpecValueService {

    @Autowired
    private GoodsSpecValueMapper goodsSpecValueMapper;


    // 批量插入商品-规格值
    @Override
    public void addGoodsSpecValue(GoodsSpecValueDTO goodsSpecValueDTO) {

        goodsSpecValueMapper.addGoodsSpecValue(goodsSpecValueDTO.getGoodsId(), goodsSpecValueDTO.getSpecValueIds());

    }
}
