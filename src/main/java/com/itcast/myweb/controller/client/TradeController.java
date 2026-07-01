package com.itcast.myweb.controller.client;


import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.domain.dto.OrderDTO;
import com.itcast.myweb.domain.dto.OrderPageDTO;
import com.itcast.myweb.domain.vo.OrderDetailVO;
import com.itcast.myweb.domain.vo.OrderVO;
import com.itcast.myweb.service.client.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交易控制器
 */
@RestController
@RequestMapping("/myweb/c/trade")
@Api(tags = "交易控制器")
@Slf4j
@RequiredArgsConstructor
public class TradeController {


    // 交易服务
    private final TradeService tradeService;


    /**
     * 创建订单
     */
    @PostMapping("/create")
    @ApiOperation(value = "创建订单")
    public Result createOrder(@RequestBody OrderDTO orderDTO) {
        log.info("创建订单: {}", orderDTO);
        return Result.ok(tradeService.createOrder(orderDTO));
    }


    /**
     * 删除订单
     */
    @PutMapping("/remove/{id}")
    @ApiOperation(value = "删除订单")
    public Result deleteOrder(@PathVariable Long id) {
        log.info("删除订单: {}", id);
        tradeService.removeById(id);
        return Result.ok();
    }


    /**
     * 获取订单详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation(value = "获取订单详情")
    public Result detail(@PathVariable Long id) {
        log.info("获取订单详情: {}", id);
        OrderDetailVO order = tradeService.detail(id);
        return Result.ok(order);
    }


    /**
     * 取消订单
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "取消订单")
    public Result cancel(@PathVariable Long id) {
        log.info("取消订单: {}", id);
        tradeService.cancel(id);
        return Result.ok();
    }


    /**
     * 确认收货
     */
    @PutMapping("/confirm/{id}")
    @ApiOperation(value = "确认收货")
    public Result confirm(@PathVariable Long id) {
        log.info("确认收货: {}", id);
        tradeService.confirm(id);
        return Result.ok();
    }


    /**
     * 条件分页查询
     */
    @PostMapping("/search")
    @ApiOperation(value = "条件分页查询")
    public Result search(@RequestBody OrderPageDTO orderPageDTO) {
        log.info("搜索订单: {}", orderPageDTO);
        PageResult<OrderVO> pageResult = tradeService.search(orderPageDTO);
        return Result.ok(pageResult);
    }


}
