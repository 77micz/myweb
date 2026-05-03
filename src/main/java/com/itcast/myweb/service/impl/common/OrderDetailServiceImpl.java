package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.OrderDetail;
import com.itcast.myweb.mapper.OrderDetailMapper;
import com.itcast.myweb.service.common.IOrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

}
