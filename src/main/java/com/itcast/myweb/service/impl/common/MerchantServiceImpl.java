package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.Merchant;
import com.itcast.myweb.mapper.MerchantMapper;
import com.itcast.myweb.service.common.IMerchantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 店铺表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements IMerchantService {

}
