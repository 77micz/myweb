package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.Category;
import com.itcast.myweb.mapper.CategoryMapper;
import com.itcast.myweb.service.common.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
