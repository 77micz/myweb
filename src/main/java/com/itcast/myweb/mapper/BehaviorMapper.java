package com.itcast.myweb.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BehaviorMapper {

    //根据用户id查询用户偏好分类（前三）
    List<Map<String, Object>> selectByUserId(Integer userIdInt);

}
