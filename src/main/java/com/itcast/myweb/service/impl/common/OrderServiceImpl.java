package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.Order;
import com.itcast.myweb.mapper.OrderMapper;
import com.itcast.myweb.service.common.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
