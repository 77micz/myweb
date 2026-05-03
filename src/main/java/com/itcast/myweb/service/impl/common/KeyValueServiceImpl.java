package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.KeyValue;
import com.itcast.myweb.mapper.KeyValueMapper;
import com.itcast.myweb.service.common.IKeyValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 属性键-属性值关联表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class KeyValueServiceImpl extends ServiceImpl<KeyValueMapper, KeyValue> implements IKeyValueService {

}
