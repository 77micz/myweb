package com.itcast.myweb.controller.client;


import com.itcast.myweb.common.pojo.Result;
import com.itcast.myweb.domain.dto.AddressDTO;
import com.itcast.myweb.domain.entity.Address;
import com.itcast.myweb.domain.vo.AddressVO;
import com.itcast.myweb.service.client.AddressService;
import com.itcast.myweb.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址控制器
 */
@RestController
@RequestMapping("/myweb/c/addresses")
@Slf4j
@Api(tags = "地址控制器")
@RequiredArgsConstructor
public class AddressController {

    /**
     * 地址服务
     */
    private final AddressService addressService;


    /**
     * 新增地址
     */
    @ApiOperation(value = "新增地址")
    @PostMapping("/add")
    public Result addAddress(@RequestBody AddressDTO addressDTO) {
        log.info("新增地址: {}", addressDTO);
        addressService.addAddress(addressDTO);
        return Result.ok();
    }


    /**
     * 用户地址列表
     */
    @ApiOperation(value = "用户地址列表")
    @GetMapping("/list")
    public Result listAddress() {
        log.info("用户地址列表:{}", UserHolder.get().getId());
        //调用服务层方法获取用户地址列表
        List<AddressVO> addressVOList = addressService.listByUserId();
        return Result.ok(addressVOList);
    }

    /**
     * 删除地址
     */
    @ApiOperation(value = "删除地址")
    @PutMapping("/delete")
    public Result deleteAddress(@RequestBody List<Long> ids) {
        log.info("删除地址:{}", ids);
        addressService.removeByIds(ids);
        return Result.ok();
    }


    /**
     * 根据id查询地址
     */
    @ApiOperation(value = "根据id查询地址")
    @GetMapping("/get")
    public Result getAddress(@RequestParam("id") Long id) {
        log.info("根据id查询地址:{}", id);
        AddressVO addressVO = addressService.getById(id);
        return Result.ok(addressVO);
    }


    /**
     * 修改地址信息
     */
    @ApiOperation(value = "修改地址信息")
    @PutMapping("/update")
    public Result updateAddress(@RequestBody AddressDTO addressDTO) {
        log.info("修改地址信息:{}", addressDTO);
        addressService.updateAddress(addressDTO);
        return Result.ok();
    }


}
