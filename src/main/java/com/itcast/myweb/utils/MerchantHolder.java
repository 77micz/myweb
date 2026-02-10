package com.itcast.myweb.utils;

import com.itcast.myweb.DTO.MerchantDTO;

public class MerchantHolder {// 封装threadlocal记录商户信息

    public static final ThreadLocal<MerchantDTO> merchant = new ThreadLocal<>();// 商户信息

    // 设置商户信息
    public static void set(MerchantDTO merchantDTO){
        merchant.set(merchantDTO);
    }

    // 获取商户信息
    public static MerchantDTO get(){
        return merchant.get();
    }

    // 删除商户信息
    public static void remove(){
        merchant.remove();
    }


}
