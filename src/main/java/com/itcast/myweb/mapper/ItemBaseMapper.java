package com.itcast.myweb.mapper;

import com.itcast.myweb.domain.entity.ItemBase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 商品基础信息表spu Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface ItemBaseMapper extends BaseMapper<ItemBase> {

}
