package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Merchant {//商户

    private Long id;//商户id

    private String merchantName;//商户名称

    private String contactName;//商户联系人

    private String contactPhone;//商户联系电话

    private String address;//商户发货地址

    private Integer isValid;//是否有效,0:无效,1:有效

    private Integer notifyType;//通知类型,1:短信,2:站内信,3:短信+站内信

    private String notifyPhone;//通知手机

    private LocalDateTime createTime;//创建时间

    private LocalDateTime updateTime;//修改时间



}
