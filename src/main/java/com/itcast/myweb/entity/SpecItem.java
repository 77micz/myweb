package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpecItem {// 规格项


    private Long id;// id

    private Long templateId;// 模板id

    private String specKey;// 规格项键称

    private String specName;// 规格项显示名称

    private Integer sort;// 排序

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
