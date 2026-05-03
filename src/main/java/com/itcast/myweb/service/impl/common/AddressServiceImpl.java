package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.Address;
import com.itcast.myweb.mapper.AddressMapper;
import com.itcast.myweb.service.common.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地址表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
