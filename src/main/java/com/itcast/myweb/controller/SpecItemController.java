package com.itcast.myweb.controller;


import com.itcast.myweb.DTO.SpecItemDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.SpecItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tianmao/b/spec/item")
@Slf4j
public class SpecItemController {// 天猫商品规格项控制类


    @Autowired
    private SpecItemService specItemService;


    // 添加规格项
    @PostMapping("/add")
    public Result addSpecItem(@RequestBody SpecItemDTO specItemDTO){

        //日志
        log.info("添加规格项:{}", specItemDTO);

        //添加规格项
        specItemService.addSpecItem(specItemDTO);


        return Result.ok();
    }



}
