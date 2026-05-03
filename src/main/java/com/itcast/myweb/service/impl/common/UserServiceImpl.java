package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.User;
import com.itcast.myweb.mapper.UserMapper;
import com.itcast.myweb.service.common.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
