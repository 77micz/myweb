package com.itcast.myweb.service.impl;

import cn.hutool.core.lang.UUID;
import com.itcast.myweb.DTO.UserDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.entity.User;
import com.itcast.myweb.mapper.UserMapper;
import com.itcast.myweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {// 用户服务实现类


    @Autowired
    private UserMapper userMapper;// 用户映射器


    //创建用户
    @Override
    public void createUser(User user) {

        //生成随机密码
        user.setPassword(UUID.randomUUID().toString().substring(0, 8));


        //生成随机昵称
        user.setNickName("nick_" + UUID.randomUUID().toString().substring(0, 8));


        //创建用户
        userMapper.createUser(user);

    }


    //获取用户
    @Override
    public UserDTO getUser(Long id) {

        //获取用户
        return userMapper.getUser(id, Constant.IS_NOT_DELETED);

    }


    //更新用户
    @Override
    public void updateUser(User user) {

        userMapper.updateUser(user);

    }


    //注销用户
    @Override
    public void logoff(Long id) {

        userMapper.updateUser(User.builder().id(id).isDeleted(Constant.LOGICAL_DELETED).build());

    }


}
