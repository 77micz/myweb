package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.RegisterDTO;
import com.itcast.myweb.entity.Merchant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MerchantMapper {



    // 商户注册
    void register(RegisterDTO registerDTO);


    // 根据手机号查询商户
    Merchant getByPhone(String contactPhone);
}
