package com.itcast.myweb.utils;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.itcast.myweb.common.pojo.OrderClazz;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 构建排序字段工具类
 */
public class OrderItemUtils {


    // 构建排序字段
    public static List<OrderItem> buildOrderItem(List<OrderClazz> orderClazzList) {    // 排序字段列表

        //判断集合是否为空
        if (orderClazzList == null || orderClazzList.isEmpty()) {
            return null;
        }


        //按照优先级进行升序排序
        orderClazzList.sort((clazz1, clazz2) -> clazz1.getPriority() - clazz2.getPriority());

        //创建排序字段列表
        return orderClazzList.stream().map(orderClazz -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(orderClazz.getColumn());
            orderItem.setAsc(orderClazz.isAsc());
            return orderItem;
        }).collect(Collectors.toList());

    }


}
