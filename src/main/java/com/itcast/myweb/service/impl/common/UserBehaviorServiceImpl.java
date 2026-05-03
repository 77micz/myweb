package com.itcast.myweb.service.impl.common;

import com.itcast.myweb.domain.entity.UserBehavior;
import com.itcast.myweb.mapper.UserBehaviorMapper;
import com.itcast.myweb.service.common.IUserBehaviorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户行为表 服务实现类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Service
public class UserBehaviorServiceImpl extends ServiceImpl<UserBehaviorMapper, UserBehavior> implements IUserBehaviorService {

}
