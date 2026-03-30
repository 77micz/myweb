package com.itcast.myweb.utils;


import com.itcast.myweb.domain.dto.UserDTO;

public class UserHolder {// 用户线程局部变量


    private static final ThreadLocal<UserDTO> USER_HOLDER = new ThreadLocal<>();// 用户线程局部变量

    // 设置用户
    public static void set(UserDTO userDTO){
        USER_HOLDER.set(userDTO);
    }

    // 获取用户
    public static UserDTO get(){
        return USER_HOLDER.get();
    }

    // 清除用户
    public static void remove(){
        USER_HOLDER.remove();
    }


}
