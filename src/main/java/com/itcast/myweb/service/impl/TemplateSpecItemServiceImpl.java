package com.itcast.myweb.service.impl;

import com.itcast.myweb.DTO.TemplateItemDTO;
import com.itcast.myweb.mapper.TemplateSpecItemMapper;
import com.itcast.myweb.service.TemplateSpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TemplateSpecItemServiceImpl implements TemplateSpecItemService {


    @Autowired
    private TemplateSpecItemMapper templateSpecItemMapper;


    // 批量插入模板-规格项
    @Override
    public void addTemplateItem(TemplateItemDTO templateItemDTO) {

        templateSpecItemMapper.addTemplateSpecItem(templateItemDTO.getSpecTemplateId(), templateItemDTO.getSpecItemIds());

    }




}
