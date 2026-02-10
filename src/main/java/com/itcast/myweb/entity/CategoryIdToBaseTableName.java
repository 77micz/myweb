package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryIdToBaseTableName {// 分类id与基础表名映射


    private Long id;// id

    private Long categoryId;// 分类id

    private String tableName;// 基础表名

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间


}
