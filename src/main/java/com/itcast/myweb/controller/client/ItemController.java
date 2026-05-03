package com.itcast.myweb.controller.client;


import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.domain.dto.ItemPageDTO;
import com.itcast.myweb.domain.vo.CategoryVO;
import com.itcast.myweb.domain.vo.ItemBaseVO;
import com.itcast.myweb.domain.vo.ItemDetailVO;
import com.itcast.myweb.service.client.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/myweb/c/items")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "商品接口")
public class ItemController {

    /**
     * 商品service
     */
    private final ItemService itemService;


    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    @GetMapping("/list/categories")
    @ApiOperation(value = "查询分类列表")
    public Result category() {

        //日志
        log.info("查询分类列表");

        //查询分类列表
        List<CategoryVO> categoryVOList = itemService.category();


        return Result.ok(categoryVOList);
    }

    /**
     * 热门商品列表
     *
     * @return 热门商品列表
     */
    @PostMapping("/page/hot")
    @ApiOperation(value = "热门商品列表")
    public Result hot(@RequestBody ItemPageDTO itemPageDTO) {
        //日志
        log.info("热门商品列表,分类id:{},页码:{}", itemPageDTO.getCategoryId(), itemPageDTO.getPageNo());
        //查询热门商品列表
        PageResult<ItemBaseVO> itemPage = itemService.pageByCategory(itemPageDTO);

        return Result.ok(itemPage);
    }


    /**
     * 查询商品详情
     *
     * @return 商品详情
     */
    @GetMapping("/get/detail")
    @ApiOperation(value = "查询商品详情")
    public Result detail(@RequestParam Long id) {
        //日志
        log.info("查询商品详情,baseId:{}", id);
        //查询商品详情
        ItemDetailVO itemDetailVO = itemService.detail(id);
        return Result.ok(itemDetailVO);
    }


    /**
     * 搜索商品
     *
     * @return 商品列表
     */
    @PostMapping("/page/search")
    @ApiOperation(value = "搜索商品")
    public Result search(@RequestBody ItemPageDTO itemPageDTO) {
        //日志
        log.info("搜索商品,分类id:{},商品名称:{},页码:{}", itemPageDTO.getCategoryId(), itemPageDTO.getName(), itemPageDTO.getPageNo());
        //查询商品列表
        PageResult<ItemBaseVO> itemPage = itemService.pageSearch(itemPageDTO);
        return Result.ok(itemPage);
    }


}
