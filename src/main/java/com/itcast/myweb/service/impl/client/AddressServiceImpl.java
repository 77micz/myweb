package com.itcast.myweb.service.impl.client;

import cn.hutool.core.bean.BeanUtil;
import com.aliyun.core.utils.StringUtils;
import com.itcast.myweb.common.exception.AddressNotFoundException;
import com.itcast.myweb.common.exception.AddressNumberInconsistentException;
import com.itcast.myweb.domain.dto.AddressDTO;
import com.itcast.myweb.domain.entity.Address;
import com.itcast.myweb.domain.entity.User;
import com.itcast.myweb.domain.vo.AddressVO;
import com.itcast.myweb.mapper.AddressMapper;
import com.itcast.myweb.service.client.AddressService;
import com.itcast.myweb.service.common.IAddressService;
import com.itcast.myweb.service.common.IUserService;
import com.itcast.myweb.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 地址服务实现类
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {


    /**
     * 地址服务
     */
    private final IAddressService addressService;

    /**
     * 用户服务
     */
    private final IUserService userService;


    /**
     * 新增地址
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addAddress(AddressDTO addressDTO) {

        //0.准备数据
        Long userId = UserHolder.get().getId();

        //判断每个字段是否为空
        judgeAddressDTO(addressDTO);


        //1.新增地址
        Address address = new Address();
        BeanUtil.copyProperties(addressDTO, address);
        address.setUserId(userId);
        addressService.save(address);

        Boolean isDefault = addressDTO.getIsDefault();
        //判断是否为默认地址
        if (isDefault != null && isDefault) {

            //设置为默认地址
            Long addressId = address.getId();

            //更新用户信息
            userService.lambdaUpdate()
                    .eq(User::getId, userId)
                    .set(User::getDefaultAddressId, addressId)
                    .update();

            //将其他用户地址取消默认
            addressService.lambdaUpdate()
                    .eq(Address::getUserId, userId)
                    .notIn(Address::getId, addressId)
                    .set(Address::getIsDefault, false)
                    .update();

        }


    }


    /**
     * 用户地址列表
     */
    @Override
    public List<AddressVO> listByUserId() {

        //0.准备数据
        Long userId = UserHolder.get().getId();


        //1.查询用户地址列表
        List<Address> list = addressService.lambdaQuery()
                .eq(Address::getUserId, userId)
                .list();


        //2.转换为VO列表
        return BeanUtil.copyToList(list, AddressVO.class);


    }


    /**
     * 删除地址
     */
    @Override
    public void removeByIds(List<Long> ids) {

        //0.准备数据
        Long userId = UserHolder.get().getId();

        //1.查询地址
        List<Address> list = addressService.lambdaQuery()
                .eq(Address::getUserId, userId)
                .in(Address::getId, ids)
                .list();

        //判断数量是否一致
        if (list.size() != ids.size()) {
            throw new AddressNumberInconsistentException("删除地址失败");
        }

        //2.删除地址
        addressService.removeByIds(ids);

    }


    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressVO getById(Long id) {

        //0.准备数据
        Long userId = UserHolder.get().getId();

        //1.查询地址
        Address address = addressService.lambdaQuery()
                .eq(Address::getUserId, userId)
                .eq(Address::getId, id)
                .one();

        //判断地址是否存在
        if (address == null) {
            throw new AddressNotFoundException("地址不存在");
        }

        //2.转换为VO
        return BeanUtil.toBean(address, AddressVO.class);
    }


    /**
     * 修改地址信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAddress(AddressDTO addressDTO) {

        //0.准备数据
        Long userId = UserHolder.get().getId();
        Long addressId = addressDTO.getId();

        //1.查询地址
        Address address = addressService.lambdaQuery()
                .eq(Address::getUserId, userId)
                .eq(Address::getId, addressId)
                .one();

        //判断地址是否存在
        if (address == null) {
            throw new AddressNotFoundException("地址不存在");
        }

        //查询用户信息
        User user = userService.lambdaQuery()
                .eq(User::getId, userId)
                .one();

        //取消默认地址
        if (Objects.equals(user.getDefaultAddressId(), addressId) && addressDTO.getIsDefault() == false) {
            userService.lambdaUpdate()
                    .eq(User::getId, userId)
                    .set(User::getDefaultAddressId, null)
                    .update();
        }

        if (!Objects.equals(user.getDefaultAddressId(), addressId) && addressDTO.getIsDefault()) {

            //更新用户信息
            userService.lambdaUpdate()
                    .eq(User::getId, userId)
                    .set(User::getDefaultAddressId, addressId)
                    .update();

            //将其他用户地址取消默认
            addressService.lambdaUpdate()
                    .eq(Address::getUserId, userId)
                    .notIn(Address::getId, addressId)
                    .set(Address::getIsDefault, false)
                    .update();
        }


        //2.更新地址
        addressService.lambdaUpdate()
                .eq(Address::getUserId, userId)
                .eq(Address::getId, addressId)
                .set(addressDTO.getReceiverName() != null, Address::getReceiverName, addressDTO.getReceiverName())
                .set(addressDTO.getReceiverPhone() != null, Address::getReceiverPhone, addressDTO.getReceiverPhone())
                .set(addressDTO.getProvinceCode() != null, Address::getProvinceCode, addressDTO.getProvinceCode())
                .set(addressDTO.getProvinceName() != null, Address::getProvinceName, addressDTO.getProvinceName())
                .set(addressDTO.getCityCode() != null, Address::getCityCode, addressDTO.getCityCode())
                .set(addressDTO.getCityName() != null, Address::getCityName, addressDTO.getCityName())
                .set(addressDTO.getDistrictCode() != null, Address::getDistrictCode, addressDTO.getDistrictCode())
                .set(addressDTO.getDistrictName() != null, Address::getDistrictName, addressDTO.getDistrictName())
                .set(addressDTO.getStreetCode() != null, Address::getStreetCode, addressDTO.getStreetCode())
                .set(addressDTO.getStreetName() != null, Address::getStreetName, addressDTO.getStreetName())
                .set(addressDTO.getDetailAddress() != null, Address::getDetailAddress, addressDTO.getDetailAddress())
                .set(addressDTO.getIsDefault() != null, Address::getIsDefault, addressDTO.getIsDefault())
                .update();

    }


    //判断每个字段是否为空
    private static void judgeAddressDTO(AddressDTO addressDTO) {
        String receiverName = addressDTO.getReceiverName();
        if (StringUtils.isBlank(receiverName)) {
            throw new IllegalArgumentException("收件人姓名不能为空");
        }
        String receiverPhone = addressDTO.getReceiverPhone();
        if (StringUtils.isBlank(receiverPhone)) {
            throw new IllegalArgumentException("收件人手机不能为空");
        }
        String provinceCode = addressDTO.getProvinceCode();
        if (StringUtils.isBlank(provinceCode)) {
            throw new IllegalArgumentException("省份编码不能为空");
        }
        String provinceName = addressDTO.getProvinceName();
        if (StringUtils.isBlank(provinceName)) {
            throw new IllegalArgumentException("省份名称不能为空");
        }
        String cityCode = addressDTO.getCityCode();
        if (StringUtils.isBlank(cityCode)) {
            throw new IllegalArgumentException("城市编码不能为空");
        }
        String cityName = addressDTO.getCityName();
        if (StringUtils.isBlank(cityName)) {
            throw new IllegalArgumentException("城市名称不能为空");
        }
        String districtCode = addressDTO.getDistrictCode();
        if (StringUtils.isBlank(districtCode)) {
            throw new IllegalArgumentException("区县编码不能为空");
        }
        String districtName = addressDTO.getDistrictName();
        if (StringUtils.isBlank(districtName)) {
            throw new IllegalArgumentException("区县名称不能为空");
        }
        String streetCode = addressDTO.getStreetCode();
        if (StringUtils.isBlank(streetCode)) {
            throw new IllegalArgumentException("街道编码不能为空");
        }
        String streetName = addressDTO.getStreetName();
        if (StringUtils.isBlank(streetName)) {
            throw new IllegalArgumentException("街道名称不能为空");
        }
        String detailAddress = addressDTO.getDetailAddress();
        if (StringUtils.isBlank(detailAddress)) {
            throw new IllegalArgumentException("详细地址不能为空");
        }
    }
}
