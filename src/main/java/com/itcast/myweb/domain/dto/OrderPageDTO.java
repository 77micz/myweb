package com.itcast.myweb.domain.dto;

import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.pojo.OrderClazz;
import com.itcast.myweb.common.pojo.PageSearch;
import com.itcast.myweb.enums.MyOrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


/**
 * 订单分页查询参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderPageDTO extends PageSearch {


    static {
        orderClazzList = new ArrayList<>();
        orderClazzList.add(new OrderClazz("create_time", false, 1));
        orderClazzList.add(new OrderClazz("id", false, 2));
    }


    //分页条件

    /**
     * 订单状态
     */
    private MyOrderStatus status = MyOrderStatus.ALL;

    /**
     * 订单ID
     */
    private Long orderId;


    /**
     * 商品标题
     */
    private String title;


    /**
     * 店铺名称
     */
    private String merchantName;


    //默认数量

    public OrderPageDTO() {
        super.setPageSize(Constant.DEFAULT_ORDER_PAGE_SIZE);
    }

    public OrderPageDTO(Long pageNo) {
        super.setPageNo(pageNo);
        super.setPageSize(Constant.DEFAULT_ORDER_PAGE_SIZE);
    }


    @Override
    public List<OrderClazz> getOrderClazzList() {
        return orderClazzList;
    }
}
