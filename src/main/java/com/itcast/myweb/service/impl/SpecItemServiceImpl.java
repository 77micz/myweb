package com.itcast.myweb.service.impl;

import com.itcast.myweb.DTO.SpecItemDTO;
import com.itcast.myweb.mapper.SpecItemMapper;
import com.itcast.myweb.service.SpecItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SpecItemServiceImpl implements SpecItemService {


    @Autowired
    private SpecItemMapper specItemMapper;

    // 添加规格项
    @Override
    public void addSpecItem(SpecItemDTO specItemDTO) {

        specItemMapper.addSpecItem(specItemDTO);

    }



}
