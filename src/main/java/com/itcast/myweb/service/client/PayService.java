package com.itcast.myweb.service.client;


import com.itcast.myweb.domain.dto.PayDTO;

/**
 * 支付服务
 */
public interface PayService {

    /**
     * 生成支付单
     */
    void createPayOrder(PayDTO payDTO);


    /**
     * 支付
     */
    void pay(PayDTO payDTO);

    /**
     * 取消支付
     */
    void cancelPay(PayDTO payDTO);
}
