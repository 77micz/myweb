package com.itcast.myweb.mapper;


import com.itcast.myweb.DTO.ItemDTO;
import com.itcast.myweb.pojo.HotSelect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;


// 商品映射器
@Mapper
public interface ItemMapper extends HotSelect<ItemDTO> {



    /**
     * 查询商品列表
     * @param count 商品数量
     * @return 商品列表
     */
    List<ItemDTO> selectHotList(Integer count);// 查询热销商品列表


    @Update("update item set recent_sales = 0")
    void resetRecentSales();

    /**
     * 根据销售量查询商品列表
     * @param count 商品数量
     * @return 商品列表
     */
    List<ItemDTO> selectBySales(Integer count);

    /**
     * 根据用户偏好查询商品列表
     * @param categoryIdList 分类id列表
     * @return 商品列表
     */
    List<ItemDTO> selectByReference(List<Long> categoryIdList,Integer count);
}
