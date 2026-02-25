package com.itcast.myweb.controller.entity;


import com.itcast.myweb.DTO.GoodsSpecValueDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.GoodsSpecValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tianmao/entity/spec/goodsvalue")
@Slf4j
public class GoodsSpecValueController {


    @Autowired
    private GoodsSpecValueService goodsSpecValueService;

    //新增商品-规格值
    @PostMapping("/add")
    public Result addGoodsSpecValue(@RequestBody GoodsSpecValueDTO goodsSpecValueDTO) {

        //日志
        log.info("新增商品-规格值:{}", goodsSpecValueDTO);

        goodsSpecValueService.addGoodsSpecValue(goodsSpecValueDTO);


        return Result.ok();

    }




}
