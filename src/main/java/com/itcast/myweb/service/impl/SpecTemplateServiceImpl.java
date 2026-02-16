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

        // 日志
        log.info("添加规格模板:{}", specTemplateDTO);

        specTemplateMapper.addSpecTemplate(specTemplateDTO);

    }


}
