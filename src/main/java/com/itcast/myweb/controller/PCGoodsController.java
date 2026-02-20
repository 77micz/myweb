package com.itcast.myweb.controller;


import com.itcast.myweb.DTO.PCGoodsDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.PCBaseService;
import com.itcast.myweb.service.PCGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tianmao/b/commodity/specific/pc")
@Slf4j
public class PCGoodsController {// PC商品控制器

    @Autowired
    private PCGoodsService pcGoodsService;


    //添加商品
    @PostMapping("/add")
    public Result addCommodity(@RequestBody PCGoodsDTO pcGoodsDTO){

        //日志
        log.info("添加商品:{}", pcGoodsDTO);

        pcGoodsService.addCommodity(pcGoodsDTO);


        return Result.ok();
    }


    //根据id查询商品
    @GetMapping("/query")
    public Result queryCommodity(@RequestParam Long id){

        //日志
        log.info("根据id查询商品:{}", id);

        PCGoodsDTO pcGoodsDTO = pcGoodsService.queryCommodity(id);

        //判断是否为空
        if (pcGoodsDTO == null){
            return Result.error("商品不存在");
        }

        return Result.ok(pcGoodsDTO);
    }


    //根据base_id查询所有goods
    @GetMapping("/list")
    public Result listCommodity(@RequestParam(name = "base_id") Long baseId){

        //日志
        log.info("根据base_id查询所有goods,baseId = {}", baseId);


        List<PCGoodsDTO> list = pcGoodsService.listCommodity(baseId);

        return Result.ok(list);
    }


    //修改商品
    @PutMapping("/update")
    public Result updateCommodity(@RequestBody PCGoodsDTO pcGoodsDTO){

        //日志
        log.info("修改商品:{}", pcGoodsDTO);

        pcGoodsService.updateCommodity(pcGoodsDTO);


        return Result.ok();
    }


    //删除商品
    @DeleteMapping("/del")
    public Result delCommodity(@RequestParam Long id){

        //日志
        log.info("删除商品:{}", id);

        pcGoodsService.delCommodity(id);


        return Result.ok();
    }









}
