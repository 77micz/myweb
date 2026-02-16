package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.SpecTemplateDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SpecTemplateMapper {


    //新增模板
    @Insert("insert into spec_template(template_name,category_id) values(#{templateName},#{categoryId})")
    void addSpecTemplate(SpecTemplateDTO specTemplateDTO);



}
