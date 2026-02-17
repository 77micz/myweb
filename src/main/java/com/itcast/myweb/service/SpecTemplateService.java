package com.itcast.myweb.service;

import com.itcast.myweb.DTO.SpecTemplateDTO;
import com.itcast.myweb.pojo.Result;

public interface SpecTemplateService {


    // 规格模板服务


    // 添加规格模板
    void addSpecTemplate(SpecTemplateDTO specTemplateDTO);


    //删除规格模板
    void softDelSpecTemplate(Long id);
}
