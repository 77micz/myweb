package com.itcast.myweb.common.pojo;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.itcast.myweb.common.Constant;
import lombok.Data;
import lombok.Setter;

import java.util.List;


//分页查询参数
@Setter
@Data
public abstract class PageSearch {

    /**
     * 页码
     */
    private Long pageNo = Constant.DEFAULT_PAGE_NO;


    /**
     * 每页数量
     */
    private Long pageSize;


    /**
     * 排序字段列表
     */
    public static List<OrderClazz> orderClazzList;


    //获取排序列表
    public abstract List<OrderClazz> getOrderClazzList();


}
