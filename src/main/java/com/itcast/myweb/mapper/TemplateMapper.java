package com.itcast.myweb.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TemplateMapper {


    //删除模板
    @Update("update spec_template set is_deleted = #{T(com.itcast.myweb.common.Constant).LOGICAL_DELETED} where id = #{id}")
    void softDelTemplate(Long id);



}
