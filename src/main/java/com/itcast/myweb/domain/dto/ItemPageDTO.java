package com.itcast.myweb.domain.dto;

import com.itcast.myweb.common.pojo.PageSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商品分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemPageDTO extends PageSearch {

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 商品名称
     */
    private String name;


    /**
     * 排序字段
     */
    private String sort = "recentSale";

    /**
     * 排序方向
     */
    private String order = "desc";


    /**
     * 是否上架
     */
    private Boolean isOnSale = Boolean.TRUE;


    /**
     * 兜底排序
     */
    private String lastSort = "id";

    /**
     * 兜底排序方向
     */
    private String lastOrder = "asc";








}
