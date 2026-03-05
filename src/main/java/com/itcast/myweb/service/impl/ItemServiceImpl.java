package com.itcast.myweb.service.impl;

import com.itcast.myweb.DTO.ItemDTO;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.mapper.ItemMapper;
import com.itcast.myweb.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// 商品服务实现类
@Service
public class ItemServiceImpl implements ItemService {


    @Autowired
    private ItemMapper itemMapper;



    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ItemDTO> updateDB() {
        // 重置热门商品的销售量为0
        itemMapper.resetRecentSales();

        //查询数据库以总销售量加载热门商品缓存
        return itemMapper.selectBySales(Constant.HOT_ITEM_COUNT);
    }
}
