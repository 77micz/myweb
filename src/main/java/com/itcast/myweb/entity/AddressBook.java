package com.itcast.myweb.entity;


import lombok.Data;

@Data
public class AddressBook {// 地址簿


    private Long id;// id

    private Long userId;// 用户id

    private String receiverName;// 收货人名称

    private String receiverPhone;// 收货人手机号

    private Integer provinceCode;// 省编码

    private String provinceName;// 省名称

    private Integer cityCode;// 市编码

    private String cityName;// 市名称

    private Integer districtCode;// 区编码

    private String districtName;// 区名称

    private String detailAddress;// 详细地址

    private Integer isDefault;// 是否默认,1:是,0:否

    private String createTime;// 创建时间

    private String updateTime;// 更新时间


}
