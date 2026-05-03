package com.itcast.myweb.mapper;

import com.itcast.myweb.domain.entity.ItemSku;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品表sku Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface ItemSkuMapper extends BaseMapper<ItemSku> {

}
