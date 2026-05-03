package com.itcast.myweb.mapper;

import com.itcast.myweb.domain.entity.PayOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 支付订单 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface PayOrderMapper extends BaseMapper<PayOrder> {

}
