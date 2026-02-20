package com.itcast.myweb.service;

import com.itcast.myweb.DTO.SpecValueDTO;
import com.itcast.myweb.entity.SpecValue;

import java.util.List;

public interface SpecValueService {// 规格值服务

    // 添加规格值
    void addSpecValue(SpecValueDTO specValueDTO);


    // 根据商品id查询关联的所有规格值
    List<SpecValue> listSpecValue(Long goodsId);


    // 根据id查询规格值
    SpecValue querySpecValue(Long id);

    // 删除规格值
    void softDelSpecValue(Long id);


    // 修改规格值
    void updateSpecValue(SpecValueDTO specValueDTO);
}
