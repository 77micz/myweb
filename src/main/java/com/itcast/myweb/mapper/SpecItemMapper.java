package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.SpecItemDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpecItemMapper {


    //添加规格项
    void addSpecItem(SpecItemDTO specItemDTO);



}
