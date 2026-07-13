package com.itcast.myweb.controller.client;


import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.domain.dto.OrderDTO;
import com.itcast.myweb.domain.dto.OrderPageDTO;
import com.itcast.myweb.domain.dto.PayDTO;
import com.itcast.myweb.domain.vo.OrderDetailVO;
import com.itcast.myweb.domain.vo.OrderVO;
import com.itcast.myweb.service.client.PayService;
import com.itcast.myweb.service.client.TradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
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

    //对用户暴露交易接口，不直接调用支付服务，而是通过交易服务调用支付服务


    /**
     * 交易服务
     */
    private final TradeService tradeService;

    /**
     * 支付服务
     */
    private final PayService payService;


    /**
     * 创建订单
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/create")
    @ApiOperation(value = "创建订单")
    public Result createOrder(@RequestBody OrderDTO orderDTO) {
        log.info("创建订单: {}", orderDTO);
        List<Long> orderIds = tradeService.createOrder(orderDTO);
        //构建payDTO
        PayDTO payDTO = new PayDTO();
        payDTO.setOrderIds(orderIds);
        payDTO.setPaymentType(orderDTO.getPaymentType());
        payDTO.setPayChannelCode(orderDTO.getPayChannelCode());
        //调用支付服务创建支付单
        payService.createPayOrder(payDTO);
        return Result.ok();
    }


    /**
     * 删除订单
     */
    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/remove/{id}")
    @ApiOperation(value = "删除订单")
    public Result deleteOrder(@PathVariable Long id) {
        log.info("删除订单: {}", id);
        tradeService.removeById(id);
        //删除支付单
        PayDTO payDTO = new PayDTO();
        payDTO.setOrderIds(List.of(id));
        payService.removePayOrder(payDTO);
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
    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "取消订单")
    public Result cancel(@PathVariable Long id) {
        log.info("取消订单: {}", id);
        tradeService.cancel(id);
        //取消支付单
        PayDTO payDTO = new PayDTO();
        payDTO.setOrderIds(List.of(id));
        payService.cancelPay(payDTO);
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
