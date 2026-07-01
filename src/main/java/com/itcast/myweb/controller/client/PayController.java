package com.itcast.myweb.controller.client;


import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.domain.dto.PayDTO;
import com.itcast.myweb.enums.PaymentType;
import com.itcast.myweb.service.client.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myweb/c/pay")
@Slf4j
@Api(tags = "支付接口")
@RequiredArgsConstructor
public class PayController {

    /**
     * payService
     */
    private final PayService payService;


    /**
     * 创建支付单
     */
    @ApiOperation("创建支付单")
    @PostMapping("/create")
    public Result createPayOrder(@RequestBody PayDTO payDTO) {
        log.info("生成支付单参数:{}", payDTO);
        if (payDTO.getPaymentType() != PaymentType.BALANCE) {
            throw new IllegalArgumentException("暂不支持余额以外支付方式");
        }
        payService.createPayOrder(payDTO);
        return Result.ok();
    }


    /**
     * 支付
     */
    @ApiOperation(value = "支付")
    @PostMapping("/pay")
    public Result pay(@RequestBody PayDTO payDTO) {
        log.info("支付参数:{}", payDTO);
        if (payDTO.getPaymentType() != com.itcast.myweb.enums.PaymentType.BALANCE) {
            throw new IllegalArgumentException("暂不支持余额以外支付方式");
        }
        payService.pay(payDTO);
        return Result.ok();
    }


    /**
     * 取消支付
     */
    @ApiOperation(value = "取消支付")
    @PostMapping("/cancel")
    public Result cancelPay(@RequestBody PayDTO payDTO) {
        log.info("取消支付参数:{}", payDTO);
        payService.cancelPay(payDTO);
        return Result.ok();
    }




}
