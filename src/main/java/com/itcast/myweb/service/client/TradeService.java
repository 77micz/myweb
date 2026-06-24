package com.itcast.myweb.service.client;


import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.domain.dto.OrderDTO;
import com.itcast.myweb.domain.dto.OrderPageDTO;
import com.itcast.myweb.domain.vo.OrderDetailVO;
import com.itcast.myweb.domain.vo.OrderVO;

import java.util.List;

/**
 * 交易服务
 */
public interface TradeService {


    /**
     * 创建订单
     */
    void createOrder(OrderDTO orderDTO);


    /**
     * 订单详情
     */
    OrderDetailVO detail(Long id);


    /**
     * 取消订单
     */
    void cancel(Long id);


    /**
     * 确认收货
     */
    void confirm(Long id);


    /**
     * 条件分页查询
     */
    PageResult<OrderVO> search(OrderPageDTO orderPageDTO);


    /**
     * 删除订单
     *
     * @param id 订单ID
     */
    void removeById(Long id);
}
