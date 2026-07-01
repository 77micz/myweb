package com.itcast.myweb.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcast.myweb.domain.dto.OrderPageDTO;
import com.itcast.myweb.domain.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itcast.myweb.domain.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author nick
 * @since 2026-03-28
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {


    /**
     * 条件分页查询
     */
//    @InterceptorIgnore(
//            others = {"com.baomidou.mybatisplus.extension.plugins.inner.LogicDeleteInnerInterceptor"}
//    )
    Page<OrderVO> selectPage(@Param("page") Page<OrderDetail> page,
                             @Param("orderPageDTO") OrderPageDTO orderPageDTO,
                             @Param("userId") Long userId,
                             @Param("status") Integer status);


    /**
     * 根据订单ID列表查询订单明细
     *
     * @param orderIds 订单ID列表
     * @return 订单明细列表
     */
    List<OrderDetail> listByOrderIds(@Param("orderIds") List<Long> orderIds,
                                     @Param("userId") Long userId,
                                     @Param("status") Integer status);


}
