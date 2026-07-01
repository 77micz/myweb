package com.itcast.myweb.mapper;

import com.itcast.myweb.domain.entity.ItemBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcast.myweb.domain.entity.PayOrderRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 支付单关联表 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface PayOrderRelationMapper extends BaseMapper<PayOrderRelation> {

}
