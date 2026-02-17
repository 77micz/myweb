package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.PCBaseDTO;
import com.itcast.myweb.DTO.PCBaseQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    @Select("select * from pc_base where id=#{id} and is_deleted=0")
    PCBaseDTO queryCommodity(Long id);
}
