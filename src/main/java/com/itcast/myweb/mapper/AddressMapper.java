package com.itcast.myweb.mapper;

import com.itcast.myweb.domain.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 * 地址表 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}
