package com.itcast.myweb.service.client;


import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.domain.dto.ItemPageDTO;
import com.itcast.myweb.domain.vo.CategoryVO;
import com.itcast.myweb.domain.vo.ItemBaseVO;
import com.itcast.myweb.domain.vo.ItemDetailVO;
import com.itcast.myweb.domain.vo.ItemSkuVO;

import java.util.List;

/**
 * 商品service
 */
public interface ItemService {

    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    List<CategoryVO> category();



    /**
     * 商品分页查询
     *
     * @param itemPageDTO 商品分页查询DTO
     * @return 商品分页查询DTO
     */
    PageResult<ItemBaseVO> pageByCategory(ItemPageDTO itemPageDTO);


    /**
     * 查询商品详情
     *
     * @param id 商品id
     * @return 商品详情
     */
    ItemDetailVO detail(Long id);


    /**
     * 搜索商品
     *
     * @param itemPageDTO 商品分页查询DTO
     * @return 商品分页查询DTO
     */
    PageResult<ItemBaseVO> pageSearch(ItemPageDTO itemPageDTO);
}
