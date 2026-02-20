package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.GoodsSpecValueDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsSpecValueMapper {//操作商品-规格值表的接口



    //根据商品id逻辑删除商品规格值
    @Update("update goods_spec_value set is_deleted = #{T(com.itcast.myweb.common.Constant).LOGICAL_DELETED} where goods_id = #{id}")
    void softDelGoodsSpecValue(Long id);


    //根据商品id返回商品规格值id
    @Select("select spec_value_id from goods_spec_value where goods_id = #{id}")
    List<Long> listSpecValueIds(Long id);


    //批量逻辑删除商品规格值
    void batchSoftDelGoodsSpecValue(List<Long> ids);


    //根据商品ids批量返回商品规格值id
    List<Long> getIdsByIds(List<Long> goodsIds);



    //批量插入商品-规格值
    void addGoodsSpecValue(Long goodsId, List<Long> specValueIds);
}
