package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.PCBaseDTO;
import com.itcast.myweb.DTO.PCBaseQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PCBaseMapper {// PC基础商品Mapper


    // 添加商品
    void addCommodity(PCBaseDTO pcBaseDTO);


    //分页查询商品
    List<PCBaseDTO> listCommodity(PCBaseQueryDTO pcBaseQueryDTO);


    //修改商品基本信息
    void updateCommodity(PCBaseDTO pcBaseDTO);

    //查询商品
    @Select("select * from pc_base where id=#{id} and is_deleted=#{IS_NOT_DELETED}")
    PCBaseDTO queryCommodity(Long id,Integer IS_NOT_DELETED);


    // 删除商品
    @Update("update pc_base set is_deleted=#{LOGICAL_DELETED} where id = #{id}")
    void softDelCommodity(Long id, Integer LOGICAL_DELETED);
}
