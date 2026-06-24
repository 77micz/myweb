package com.itcast.myweb.common.pojo;


import lombok.Data;

/**
 * 排序类
 */
@Data
public class OrderClazz {

    // 排序字段
    private String column;

    // 排序方式
    private boolean isAsc;

    // 排序优先级
    private Integer priority;


    public OrderClazz(String column, boolean isAsc, Integer priority) {
        this.column = column;
        this.isAsc = isAsc;
        this.priority = priority;
    }


}
