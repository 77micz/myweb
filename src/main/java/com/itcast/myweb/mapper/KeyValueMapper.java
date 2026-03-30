package com.itcast.myweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcast.myweb.domain.entity.Address;
import com.itcast.myweb.domain.entity.KeyValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 属性键-属性值 表 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface KeyValueMapper extends BaseMapper<KeyValue> {

}
