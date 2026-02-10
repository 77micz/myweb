package com.itcast.myweb.entity;


import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class OrderNotify {// 订单通知


    private Long id;// id

    private String orderId;// 订单id

    private Long merchantId;// 商户id

    private String notifyType;// 通知类型,1=短信，2=站内信，3=都发

    private String notifyStatus;// 通知状态,0=未发送，1=已发送，2=发送失败

    private String notifyContent;// 通知内容

    private LocalDateTime sendTime;// 发送时间

    private LocalDateTime createTime;// 创建时间


}
