package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PCGoods {// PC具体商品


    private Long id;// 商品ID

    private Long baseId;// 基础商品ID

    private Long goodsCode;// 商品编码

    private Long merchantId;// 商户ID

    private Double price;// 商品价格

    private Integer stock;// 商品库存

    private Integer isDeleted;// 是否删除,0:未删除,1:已删除

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
