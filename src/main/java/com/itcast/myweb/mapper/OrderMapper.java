package com.itcast.myweb.mapper;

import com.itcast.myweb.domain.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {


    /**
     * 查询已删除的订单
     */
    List<Order> selectDeletedOrders(@Param("userId") Long userId, @Param("orderId") Long orderId, @Param("detailIds") List<Long> detailIds);


}
