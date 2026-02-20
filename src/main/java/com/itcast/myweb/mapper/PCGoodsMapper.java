package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.PCGoodsDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PCGoodsMapper {

    //新增商品
    @Insert("insert into pc_goods(base_id,goods_code,merchant_id,price,stock) values(#{baseId},#{goodsCode},#{merchantId},#{price},#{stock})")
    void addGoods(PCGoodsDTO pcGoodsDTO);


    //查询商品
    @Select("select * from pc_goods where id = #{id} and is_deleted=#{T(com.itcast.myweb.common.Constant).IS_NOT_DELETED}")
    PCGoodsDTO queryGoods(Long id);


    //查询baseId所有商品
    List<PCGoodsDTO> listGoods(Long baseId);


    //修改商品
    void updateGoods(PCGoodsDTO pcGoodsDTO);


    //软删除
    @Update("update pc_goods set is_deleted=#{T(com.itcast.myweb.common.Constant).LOGICAL_DELETED} where id = #{id}")
    void softDelGoods(Long id);

    //批量删除
    @Update("update pc_goods set is_deleted=#{T(com.itcast.myweb.common.Constant).LOGICAL_DELETED} where base_id = #{baseId}")
    void batchSoftDelGoods(Long baseId);



    //批量查询
    @Select("select id from pc_goods where base_id = #{id}")
    List<Long> getIdsByBaseId(Long id);
}
