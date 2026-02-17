package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpecValue {


    private Long id;// id

    private Long templateId;// 模板id

    private Long specItemId;// 规格项id

    private String specValue;// 规格值

    private Integer sort=0;// 展示排序

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
