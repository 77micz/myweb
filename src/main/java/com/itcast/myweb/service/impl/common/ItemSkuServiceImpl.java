package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.ItemSku;
import com.itcast.myweb.mapper.ItemSkuMapper;
import com.itcast.myweb.service.common.IItemSkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表sku 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class ItemSkuServiceImpl extends ServiceImpl<ItemSkuMapper, ItemSku> implements IItemSkuService {

}
