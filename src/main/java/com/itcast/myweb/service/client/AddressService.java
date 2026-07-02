package com.itcast.myweb.service.client;


import com.itcast.myweb.domain.dto.AddressDTO;
import com.itcast.myweb.domain.entity.Address;
import com.itcast.myweb.domain.vo.AddressVO;

import java.util.List;

/**
 * 地址服务
 */
public interface AddressService {


    /**
     * 新增地址
     */
    void addAddress(AddressDTO addressDTO);

    /**
     * 用户地址列表
     */
    List<AddressVO> listByUserId();

    /**
     * 删除地址
     */
    void removeByIds(List<Long> ids);

    /**
     * 根据id查询地址
     */
    AddressVO getById(Long id);

    /**
     * 修改地址信息
     */
    void updateAddress(AddressDTO addressDTO);
}
