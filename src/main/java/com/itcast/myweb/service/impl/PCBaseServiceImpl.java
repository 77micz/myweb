package com.itcast.myweb.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.myweb.DTO.PCBaseDTO;
import com.itcast.myweb.DTO.PCBasePageDTO;
import com.itcast.myweb.DTO.PCBaseQueryDTO;
import com.itcast.myweb.mapper.PCBaseMapper;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.PCBaseService;
import com.itcast.myweb.utils.MerchantHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class PCBaseServiceImpl implements PCBaseService {// PC基础商品服务实现类


//    @Autowired
    private PCBaseMapper pcBaseMapper;

    public PCBaseServiceImpl(PCBaseMapper pcBaseMapper) {
        this.pcBaseMapper = pcBaseMapper;
    }


    // 添加商品
    @Transactional
    @Override
    public Result addCommodity(PCBaseDTO pcBaseDTO) {

        // 日志
        log.info("添加商品:{}", pcBaseDTO);

        //设置商户id
//        pcBaseDTO.setMerchantId(MerchantHolder.get().getId());
        pcBaseDTO.setMerchantId(1L);

        //添加商品
        pcBaseMapper.addCommodity(pcBaseDTO);

        return Result.ok();
    }


    //分页查询商品
    @Override
    public Result listCommodity(PCBaseQueryDTO pcBaseQueryDTO) {

        //日志
        log.info("分页查询商品,页码：{}，每页记录数：{}", pcBaseQueryDTO.getPageNum(), pcBaseQueryDTO.getPageSize());

        //分页
        PageHelper.startPage(pcBaseQueryDTO.getPageNum(), pcBaseQueryDTO.getPageSize());

        //查询商品
        Page<PCBaseDTO> list = (Page<PCBaseDTO>) pcBaseMapper.listCommodity(pcBaseQueryDTO);


        //封装为PCBasePageDTO
        return Result.ok(new PCBasePageDTO(list.getTotal(),list.getResult()));

    }


    //修改商品基本信息
    @Override
    public Result updateCommodity(PCBaseDTO pcBaseDTO) {

        // 日志
        log.info("修改商品基本信息:{}", pcBaseDTO);

        //修改商品
        pcBaseMapper.updateCommodity(pcBaseDTO);

        return Result.ok();

    }


    // 删除商品
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteCommodity(Long id) {


        //日志
        log.info("删除商品,id={}", id);

        // TODO 删除商品





        return Result.ok();
    }


}
