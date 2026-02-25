package com.itcast.myweb.controller.entity;


import com.itcast.myweb.DTO.PCBaseDTO;
import com.itcast.myweb.DTO.PCBaseQueryDTO;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.PCBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tianmao/entity/commodity/base/pc")
@Slf4j
public class PCBaseController {


    @Autowired
    private PCBaseService pcBaseService;


    // 添加商品
    @PostMapping("/add")
    public Result addCommodity(@RequestBody PCBaseDTO pcBaseDTO){
        return pcBaseService.addCommodity(pcBaseDTO);
    }


    //根据id查询
    @GetMapping("/query")
    public Result queryCommodity(@RequestParam Long id){

        // 日志
        log.info("根据id查询商品:{}", id);

        //查询商品
        return pcBaseService.queryCommodity(id);
    }


    //分页查询商品
    @PostMapping("/list")
    public Result listCommodity(@RequestBody PCBaseQueryDTO pcBaseQueryDTO){

        // TODO 添加缓存

        return pcBaseService.listCommodity(pcBaseQueryDTO);
    }


    //修改商品基本信息
    @PutMapping("/update")
    public Result updateCommodity(@RequestBody PCBaseDTO pcBaseDTO){
        return pcBaseService.updateCommodity(pcBaseDTO);
    }



    //删除基础商品
    @DeleteMapping("/delete/{id}")
    public Result deleteCommodity(@PathVariable Long id){
        return pcBaseService.softDeleteCommodity(id);
    }






}
