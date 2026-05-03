package com.itcast.myweb.common.pojo;


import lombok.Builder;
import lombok.Data;

import java.util.List;

//分页查询结果
@Data
@Builder
public class PageResult<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 分页数据
     */
    private List<T> records;


}
