package com.itcast.myweb.domain.dto;


import com.itcast.myweb.enums.PayType;
import com.itcast.myweb.enums.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PayDTO {


    //支付渠道
    private PaymentType paymentType;


    // -----------------------------


    //订单ID列表
    private List<Long> orderIds;

//    //支付方式
//    private PayType payType;


    //支付渠道编码
    private String payChannelCode;


    // ---------------------------

    //支付单号
    private List<Long> payOrderNos;

    //支付密码
    private String payPw;


}
