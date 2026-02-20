package com.itcast.myweb.service.impl;

import com.itcast.myweb.DTO.SpecValueDTO;
import com.itcast.myweb.entity.SpecValue;
import com.itcast.myweb.mapper.SpecValueGoodsMapper;
import com.itcast.myweb.mapper.SpecValueMapper;
import com.itcast.myweb.service.SpecValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class SpecValueServiceImpl implements SpecValueService {


    @Autowired
    private SpecValueMapper specValueMapper;

    @Autowired
    private SpecValueGoodsMapper specValueGoodsMapper;// 规格值商品关联


    // 添加规格值
    @Override
    public void addSpecValue(SpecValueDTO specValueDTO) {

        specValueMapper.addSpecValue(specValueDTO);

    }


    // 查询商品规格值
    @Override
    public List<SpecValue> listSpecValue(Long goodsId) {

        //查询所有商品规格值id
        List<Long> specValueIds = specValueGoodsMapper.listSpecValueIds(goodsId);

        //根据商品规格值id查询商品规格值
        List<SpecValue> specValues = specValueMapper.listSpecValue(specValueIds);

        //根据specvalue中的sort字段进行流排序，升序
        return specValues.stream().sorted((o1, o2) -> o1.getSort() - o2.getSort())
                .toList();


    }


    // 根据id查询规格值
    @Override
    public SpecValue querySpecValue(Long id) {

        SpecValue specValue = specValueMapper.querySpecValue(id);

        //判断是否为空
        if (specValue == null){
            //抛出异常
            throw new NoSuchElementException("没有此规格值");
        }


        return specValue;

    }

    // 删除规格值
    @Override
    public void softDelSpecValue(Long id) {

        //删除
        specValueMapper.softDelSpecValue(id);

    }


    // 修改规格值
    @Override
    public void updateSpecValue(SpecValueDTO specValueDTO) {

        // 修改
        specValueMapper.updateSpecValue(specValueDTO);

    }


}
