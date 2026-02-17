package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.SpecValueDTO;
import com.itcast.myweb.entity.SpecValue;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SpecValueMapper {

    // 新增规格值
    void addSpecValue(SpecValueDTO specValueDTO);


    //根据ids查询规格值
    List<SpecValue> listSpecValue(List<Long> ids);


    //根据id查询规格值
    @Select("select * from spec_value where id = #{id}")
    SpecValue querySpecValue(Long id);


    // 删除规格值
    @Delete("delete from spec_value where id = #{id}")
    void delSpecValue(Long id);


    // 修改规格值
    void updateSpecValue(SpecValueDTO specValueDTO);
}
