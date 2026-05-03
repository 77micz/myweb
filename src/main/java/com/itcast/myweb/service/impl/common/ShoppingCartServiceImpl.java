package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.ShoppingCart;
import com.itcast.myweb.mapper.ShoppingCartMapper;
import com.itcast.myweb.service.common.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

}
