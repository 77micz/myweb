package com.itcast.myweb.service;

import com.itcast.myweb.DTO.PCGoodsDTO;

import java.util.List;

public interface PCGoodsService {// PC商品服务

    //添加商品
    void addCommodity(PCGoodsDTO pcGoodsDTO);


    //查询商品
    PCGoodsDTO queryCommodity(Long id);


    //查询商品列表
    List<PCGoodsDTO> listCommodity(Long baseId);

    //修改商品
    void updateCommodity(PCGoodsDTO pcGoodsDTO);


    //删除商品
    void delCommodity(Long id);

    //恢复商品
    void restoreCommodity(Long id);
}
