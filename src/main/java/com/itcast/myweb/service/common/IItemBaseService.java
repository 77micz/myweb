package com.itcast.myweb.service.common;

import com.itcast.myweb.domain.dto.ItemDTO;
import com.itcast.myweb.domain.entity.ItemBase;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品基础信息表spu 服务类
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
public interface IItemBaseService extends IService<ItemBase> {

    //更新数据库
    public List<ItemDTO> updateDB();



}
