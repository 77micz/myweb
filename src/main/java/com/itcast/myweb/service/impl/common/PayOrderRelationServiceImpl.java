package com.itcast.myweb.service.impl.common;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itcast.myweb.domain.entity.PayOrderRelation;
import com.itcast.myweb.mapper.PayOrderRelationMapper;
import com.itcast.myweb.service.common.IPayOrderRelationService;
import org.springframework.stereotype.Service;

/**
 * 支付单关联表 服务类
 * </p>
 */

@Service
public class PayOrderRelationServiceImpl extends ServiceImpl<PayOrderRelationMapper, PayOrderRelation> implements IPayOrderRelationService {
}
