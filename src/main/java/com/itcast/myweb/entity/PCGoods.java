package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PCGoods {// PC具体商品


    private Long id;// 商品ID

    private Long baseId;// 基础商品ID

    private String goodsName;// 商品名称

    private Long merchantId;// 商户ID

    private String image;// 商品图片

    private Double price;// 商品价格

    private Integer stock;// 商品库存

    private String specCombination;// 商品规格组合

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
