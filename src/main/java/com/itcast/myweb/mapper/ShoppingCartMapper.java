package com.itcast.myweb.mapper;

import com.itcast.myweb.domain.entity.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}
