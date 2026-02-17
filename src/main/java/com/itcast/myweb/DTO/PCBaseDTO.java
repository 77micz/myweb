package com.itcast.myweb.DTO;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PCBaseDTO {// PC基础商品DTO

    private Long id;// 商品id

    private Long merchantId;// 商户id

    private String name;// 商品名称

    private String image;// 商品图片

    private Double basePrice;// 商品参考价格

    private Long categoryId;// 商品分类id

    private Long templateId;// 商品规格模板id

    private String isOnSale;// 是否上架,1:上架,0:下架

    private Integer isDeleted;// 是否删除,1:未删除,0:已删除

    private LocalDateTime deletedTime;// 删除时间

    private Long deletedBy;// 删除人id


}
