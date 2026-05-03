package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.OrderNotify;
import com.itcast.myweb.mapper.OrderNotifyMapper;
import com.itcast.myweb.service.common.IOrderNotifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单消息表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class OrderNotifyServiceImpl extends ServiceImpl<OrderNotifyMapper, OrderNotify> implements IOrderNotifyService {

}
