package com.itcast.myweb.mapper;

import com.itcast.myweb.DTO.CategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {


    // 查询所有分类
    List<CategoryDTO> selectList();


}
