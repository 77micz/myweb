package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.UserDTO;
import com.itcast.myweb.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import static com.itcast.myweb.common.Constant.IS_NOT_DELETED;

@Mapper
public interface UserMapper {// 用户Mapper


    //创建用户
    Integer createUser(User  user);


    //获取用户
    UserDTO getUser(Long id,Integer status);

    //更新用户
    void updateUser(User user);


    //根据手机号获取用户
    UserDTO getUserByPhone(@Param("phone") String phone,@Param("status") Integer status);





}
