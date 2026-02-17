package com.itcast.myweb.service;

import com.itcast.myweb.DTO.PCBaseDTO;
import com.itcast.myweb.DTO.PCBaseQueryDTO;
import com.itcast.myweb.pojo.Result;

public interface PCBaseService {// PC基础商品服务


    // 添加商品
    Result addCommodity(PCBaseDTO pcBaseDTO);


    //分页查询商品
    Result listCommodity(PCBaseQueryDTO pcBaseQueryDTO);


    //修改商品基本信息
    Result updateCommodity(PCBaseDTO pcBaseDTO);

    //删除商品
    Result deleteCommodity(Long id);

    //根据id查询商品
    Result queryCommodity(Long id);
}
