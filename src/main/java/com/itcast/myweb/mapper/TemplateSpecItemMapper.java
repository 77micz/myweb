package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.TemplateItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TemplateSpecItemMapper {

    //软删除
    @Update("update spec_template_spec_item set is_deleted = #{T(com.itcast.myweb.common.Constant).LOGICAL_DELETED} where spec_template_id = #{id}")
    void softDelTemplateSpecValue(Long id);



    // 批量插入模板-规格项
    void addTemplateSpecItem(Long specTemplateId, List<Long> specItemIds);





}
