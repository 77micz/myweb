package com.itcast.myweb.mapper;


import com.itcast.myweb.entity.SpecValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecValueGoodsMapper {// 规格值商品关联


    //根据商品id查询规格值
    @Select("select spec_value_id from goods_spec_value where goods_id = #{goodsId}")
    List<Long> listSpecValueIds(Long goodsId);



}
