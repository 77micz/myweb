package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpecTemplate {// 规格模板


    private Long id;// id

    private String templateName;// 模板名称

    private Long categoryId;// 分类id

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
