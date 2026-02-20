package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.SpecTemplateDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SpecTemplateMapper {


    //新增模板
    void addSpecTemplate(SpecTemplateDTO specTemplateDTO);


    //删除模板
    @Update("update spec_template set is_deleted = #{T(com.itcast.myweb.common.Constant).LOGICAL_DELETED} where id = #{id}")
    void softDelSpecTemplate(Long id);
}
