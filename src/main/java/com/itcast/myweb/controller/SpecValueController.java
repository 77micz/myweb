package com.itcast.myweb.controller;


import com.itcast.myweb.DTO.SpecValueDTO;
import com.itcast.myweb.entity.SpecValue;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.SpecValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tianmao/b/spec/value")
@Slf4j
public class SpecValueController {// 天猫商品规格值控制类

    @Autowired
    private SpecValueService specValueService;

    // 添加规格值
    @PostMapping("/add")
    public Result addSpecValue(@RequestBody SpecValueDTO specValueDTO){

        // 日志
        log.info("添加规格值:{}", specValueDTO);

        // 添加规格值
        specValueService.addSpecValue(specValueDTO);

        return Result.ok();
    }


    //根据商品id查询关联的所有规格值
    @GetMapping("/list")
    public Result listSpecValue(Long goodsId){

        // 日志
        log.info("根据商品id查询关联的所有规格值:{}", goodsId);

        // 查询
        List<SpecValue> list = specValueService.listSpecValue(goodsId);

        return Result.ok(list);
    }



    //根据id查询规格值
    @GetMapping("/query")
    public Result querySpecValue(@RequestParam Long id){

        // 日志
        log.info("根据id查询规格值:{}", id);

        // 查询
        SpecValue specValue = specValueService.querySpecValue(id);

        return Result.ok(specValue);
    }



    // 根据id删除规格值
    @DeleteMapping("/del")
    public Result delSpecValue(@RequestParam Long id){

        // 日志
        log.info("根据id删除规格值:{}", id);

        // 删除
        specValueService.softDelSpecValue(id);

        return Result.ok();
    }

    //修改规格值
    @PutMapping("/update")
    public Result updateSpecValue(@RequestBody SpecValueDTO specValueDTO){

        // 日志
        log.info("修改规格值:{}", specValueDTO);

        // 修改
        specValueService.updateSpecValue(specValueDTO);

        return Result.ok();
    }





}
