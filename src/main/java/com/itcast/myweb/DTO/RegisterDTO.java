package com.itcast.myweb.DTO;


import lombok.Data;

@Data
public class RegisterDTO {// 注册DTO


    private String merchantName;// 商户名称

    private String contactName;// 商户联系人

    private String contactPhone;// 商户联系电话

    private String address;// 商户发货地址

    private Integer notifyType;// 通知类型,1:短信,2:站内信,3:短信+站内信

    private String notifyPhone;// 通知手机





}
