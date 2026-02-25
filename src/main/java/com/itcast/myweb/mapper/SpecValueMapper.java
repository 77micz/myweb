package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.SpecValueDTO;
import com.itcast.myweb.entity.SpecValue;
import org.apache.ibatis.annotations.*;

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


    // 删除规格值 软删除
    @Update("update spec_value set is_deleted=#{LOGICAL_DELETED} where id=#{id}")
    void softDelSpecValue(Long id, Integer LOGICAL_DELETED);


    //批量删除
    void batchSoftDelSpecValue(List<Long> ids);


    // 修改规格值
    void updateSpecValue(SpecValueDTO specValueDTO);
}
