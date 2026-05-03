package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.AttrValue;
import com.itcast.myweb.mapper.AttrValueMapper;
import com.itcast.myweb.service.common.IAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品属性值表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class AttrValueServiceImpl extends ServiceImpl<AttrValueMapper, AttrValue> implements IAttrValueService {

}
