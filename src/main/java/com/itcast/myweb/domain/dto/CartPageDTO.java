package com.itcast.myweb.domain.dto;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.pojo.OrderClazz;
import com.itcast.myweb.common.pojo.PageSearch;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车分页数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CartPageDTO extends PageSearch {

    static {
        orderClazzList = new ArrayList<>();
        orderClazzList.add(new OrderClazz("create_time", false, 1));
        orderClazzList.add(new OrderClazz("id", false, 2));
    }


    public CartPageDTO() {
        super.setPageSize(Constant.DEFAULT_CART_PAGE_SIZE);
    }

    public CartPageDTO(Long pageNo) {
        super.setPageNo(pageNo);
        super.setPageSize(Constant.DEFAULT_CART_PAGE_SIZE);
    }


    @Override
    public List<OrderClazz> getOrderClazzList() {
        return orderClazzList;
    }

    @Override
    public void setPageSize(Long pageSize) {
        super.setPageSize(pageSize);
    }
}
