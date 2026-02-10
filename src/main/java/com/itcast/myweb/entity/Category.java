package com.itcast.myweb.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {// 商品分类

    private Long id;// id

    private String name;// 分类名称

    private LocalDateTime createTime;// 创建时间

    private LocalDateTime updateTime;// 更新时间

}
