package com.itcast.myweb.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PCBase extends Item {// PC基础商品




    private Long templateId;// 使用的规格模板id

    private Integer isDeleted;// 是否删除,0:未删除,1:已删除

    private LocalDateTime deletedTime;// 删除时间

    private Long deletedBy;// 删除人id

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
