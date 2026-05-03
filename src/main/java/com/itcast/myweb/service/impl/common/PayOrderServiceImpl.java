package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.PayOrder;
import com.itcast.myweb.mapper.PayOrderMapper;
import com.itcast.myweb.service.common.IPayOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付订单 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

}
