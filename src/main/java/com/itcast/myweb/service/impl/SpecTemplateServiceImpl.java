package com.itcast.myweb.service.impl;

import com.itcast.myweb.DTO.SpecTemplateDTO;
import com.itcast.myweb.mapper.SpecTemplateMapper;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.SpecTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SpecTemplateServiceImpl implements SpecTemplateService {


    @Autowired
    private SpecTemplateMapper specTemplateMapper;


    // 添加规格模板
    @Override
    public void addSpecTemplate(SpecTemplateDTO specTemplateDTO) {

        specTemplateMapper.addSpecTemplate(specTemplateDTO);

    }


    // 删除规格模板
    @Override
    public void softDelSpecTemplate(Long id) {

        //设置删除状态

        //调用删除
        specTemplateMapper.softDelSpecTemplate(id);

    }


}
