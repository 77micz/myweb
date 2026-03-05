package com.itcast.myweb.service;

import com.itcast.myweb.DTO.CategoryDTO;
import com.itcast.myweb.DTO.ItemDTO;
import com.itcast.myweb.entity.Item;

import java.util.List;

public interface IndexService {


    //获取分类列表
    List<CategoryDTO> listCategory();

    //获取热门商品列表
    List<ItemDTO> getHotGoodsList(Integer count);

    //根据用户偏好获取商品列表
    List<ItemDTO> getGoodsListByPreference(String token);
}
