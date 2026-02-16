package com.itcast.myweb.controller;


import com.itcast.myweb.DTO.SpecTemplateDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.SpecTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tianmao/b/spec/template")
@Slf4j
public class SpecTemplateController {// 天猫商品规格模板控制类


    @Autowired
    private SpecTemplateService specTemplateService;


    //添加规格模板
    @PostMapping("/add")
    public Result addSpecTemplate(@RequestBody SpecTemplateDTO specTemplateDTO){

        // 日志
        log.info("添加规格模板:{}", specTemplateDTO);

        //添加规格模板
        specTemplateService.addSpecTemplate(specTemplateDTO);

        return Result.ok();
    }


}
