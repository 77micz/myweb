package com.itcast.myweb.service;

import com.itcast.myweb.DTO.UserDTO;
import com.itcast.myweb.entity.User;

public interface UserService {//用户service




    //创建用户
    void createUser(User user);


    //获取用户
    UserDTO getUser(Long id);


    //修改用户信息
    void updateUser(User user);


    //注销用户
    void logoff(Long id);
}
