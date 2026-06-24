package com.itcast.myweb.domain.dto;

import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.pojo.OrderClazz;
import com.itcast.myweb.common.pojo.PageSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemPageDTO extends PageSearch {


    static {
        orderClazzList = new ArrayList<>();
        orderClazzList.add(new OrderClazz("recent_sales", false, 1));
        orderClazzList.add(new OrderClazz("sales", false, 2));
        orderClazzList.add(new OrderClazz("id", true, 3));
    }


    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 是否上架
     */
    private Boolean onSale = Boolean.TRUE;


    public ItemPageDTO() {
        super.setPageSize(Constant.DEFAULT_ITEM_PAGE_SIZE);
    }

    public ItemPageDTO(Long pageNo, Long pageSize) {
        super.setPageNo(pageNo);
        super.setPageSize(pageSize);
    }


    @Override
    public List<OrderClazz> getOrderClazzList() {
        return orderClazzList;
    }
}
