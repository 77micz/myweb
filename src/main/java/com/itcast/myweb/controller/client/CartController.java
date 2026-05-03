package com.itcast.myweb.controller.client;


import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.common.pojo.PageSearch;
import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.domain.dto.CartDTO;
import com.itcast.myweb.domain.vo.CartVO;
import com.itcast.myweb.domain.vo.ItemDetailVO;
import com.itcast.myweb.service.client.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/myweb/c/carts")
@Api(tags = "购物车控制器")
@Slf4j
@RequiredArgsConstructor
public class CartController {


    /**
     * 购物车服务
     */
    private final CartService cartService;


    /**
     * 添加购物车
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加购物车")
    public Result addCart(@RequestBody CartDTO cartDTO) {
        log.info("添加购物车: {}", cartDTO);
        cartService.addCart(cartDTO);
        return Result.ok();
    }

    /**
     * 分页查询购物车
     */
    @GetMapping("/page/list")
    @ApiOperation(value = "分页查询购物车")
    public Result pageList(PageSearch pageSearch) {
        log.info("分页查询购物车: {}", pageSearch);
        PageResult<CartVO> pageResult = cartService.pageList(pageSearch);
        return Result.ok(pageResult);
    }



    /**
     * 修改购物车数量
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改购物车数量")
    public Result updateNum(@RequestBody CartDTO cartDTO) {
        log.info("修改购物车数量: {}", cartDTO);
        cartService.updateNum(cartDTO);
        return Result.ok();
    }


    /**
     * 删除购物车
     */
    @PutMapping("/remove/{id}")
    @ApiOperation(value = "删除购物车")
    public Result deleteCart(@PathVariable Long id) {
        log.info("删除购物车: {}", id);
        cartService.deleteCart(id);
        return Result.ok();
    }


    /**
     * 进入商品详情页
     */
    @GetMapping("/get/detail")
    @ApiOperation(value = "进入商品详情页")
    public Result detail(Long id) {
        log.info("进入商品详情页: {}", id);
        ItemDetailVO itemDetailVO = cartService.detail(id);
        return Result.ok(itemDetailVO);
    }










}
