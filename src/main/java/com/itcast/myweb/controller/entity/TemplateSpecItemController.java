package com.itcast.myweb.controller.entity;


import com.itcast.myweb.DTO.TemplateItemDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.TemplateSpecItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tianmao/entity/spec/templateitem")
@Slf4j
public class TemplateSpecItemController {

    @Autowired
    private TemplateSpecItemService templateSpecItemService;


    // 添加模板-规格项
    @PostMapping("/add")
    public Result addTemplateItem(@RequestBody TemplateItemDTO templateItemDTO) {

        //日志
        log.info("添加模板-规格项:{}", templateItemDTO);

        templateSpecItemService.addTemplateItem(templateItemDTO);

        return Result.ok();

    }






}
