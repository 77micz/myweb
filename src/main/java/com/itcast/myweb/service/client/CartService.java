package com.itcast.myweb.service.client;


import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.common.pojo.PageSearch;
import com.itcast.myweb.domain.dto.CartDTO;
import com.itcast.myweb.domain.dto.CartPageDTO;
import com.itcast.myweb.domain.vo.CartVO;
import com.itcast.myweb.domain.vo.ItemDetailVO;

import java.util.List;

/**
 * 购物车服务
 */
public interface CartService {


    /**
     * 添加购物车
     */
    void addCart(CartDTO cartDTO);



    /**
     * 分页查询购物车
     */
    PageResult<CartVO> pageList(CartPageDTO cartPageDTO);

    /**
     * 修改购物车数量
     */
    void updateNum(CartDTO cartDTO);

//    /**
//     * 删除购物车
//     */
//    void deleteCart(Long id);


    /**
     * 进入商品详情页
     */
    ItemDetailVO detail(Long id);


    /**
     * 批量删除购物车
     */
    void batchRemove(List<Long> ids);
}
