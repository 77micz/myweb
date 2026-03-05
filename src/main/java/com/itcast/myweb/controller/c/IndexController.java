package com.itcast.myweb.controller.c;


import com.itcast.myweb.DTO.CategoryDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.entity.Category;
import com.itcast.myweb.mapper.CategoryMapper;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tianmao/c/index")
@Slf4j
public class IndexController {// 首页控制类



    @Autowired
    private IndexService indexService;





    /**
     * 获取分类列表
     * @return result
     */
    @GetMapping("/category")
    public Result getCategoryList(){


        log.info("获取分类列表");

        //获取分类列表
        List<CategoryDTO> categoryDTOList = indexService.listCategory();

        //判断是否为空
        if (categoryDTOList == null || categoryDTOList.isEmpty()) {
            return Result.error("获取分类列表失败");
        }


        return Result.ok(categoryDTOList);
    }


    /**
     * 根据用户偏好获取商品列表
     * @return result
     */
    @GetMapping("/preference")
    public Result getGoodsListByPreference(@RequestHeader(
            value = "Authorization",
            required = false
    ) String token){

        log.info("根据用户偏好获取商品列表,token:{}",token);

        //判断token是否为空
        if (token == null || token.isEmpty()) {
            //从热门商品获取
            return Result.ok(indexService.getHotGoodsList(Constant.HOT_ITEM_COUNT_FRONT));
        }

        //从用户偏好获取
        return Result.ok(indexService.getGoodsListByPreference(token));

    }



}
