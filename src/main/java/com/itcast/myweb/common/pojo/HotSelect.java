package com.itcast.myweb.common.pojo;



import java.util.List;

/**
 * 热销商品选择器
 */
public interface HotSelect <T>{

    /**
     * 查询热销商品列表
     * @param count 商品数量
     * @return 商品列表
     */
    public List<T> selectHotList(Integer count);

}
