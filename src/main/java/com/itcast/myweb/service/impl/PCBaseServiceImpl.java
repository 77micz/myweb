package com.itcast.myweb.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itcast.myweb.DTO.PCBaseDTO;
import com.itcast.myweb.DTO.PCBasePageDTO;
import com.itcast.myweb.DTO.PCBaseQueryDTO;
import com.itcast.myweb.mapper.*;
import com.itcast.myweb.pojo.Result;
import com.itcast.myweb.service.PCBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class PCBaseServiceImpl implements PCBaseService {// PC基础商品服务实现类


    @Autowired
    private PCBaseMapper pcBaseMapper;// PC基础商品Mapper

    @Autowired
    private PCGoodsMapper pcGoodsMapper;// PC商品Mapper



    @Autowired
    private GoodsSpecValueMapper goodsSpecValueMapper;//Goods-SpecValue表Mapper

    @Autowired
    private SpecValueMapper specValueMapper;//SpecValue表Mapper


    @Autowired
    private TemplateSpecItemMapper templateSpecItemMapper;//Template-SpecItem表Mapper

    @Autowired
    private TemplateMapper templateMapper;//Template表Mapper


//    public PCBaseServiceImpl(PCBaseMapper pcBaseMapper) {
//        this.pcBaseMapper = pcBaseMapper;
//    }


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



    /**
     * 删除商品
     * 软删除
     * @param id 商品id
     * @return 删除结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result softDeleteCommodity(Long id) {


        //日志
        log.info("删除商品,id={}", id);


        //1.设置goods与spec_value为删除状态

        //获取所有商品id
        List<Long> goodsIds = pcGoodsMapper.getIdsByBaseId(id);

        //获取所有商品规格值id
        List<Long> specValueIds = goodsSpecValueMapper.getIdsByIds(goodsIds);

        //批量逻辑删除
        goodsSpecValueMapper.batchSoftDelGoodsSpecValue(goodsIds);



        //2.设置spec_value为删除状态
        specValueMapper.batchSoftDelSpecValue(specValueIds);




        //3.设置goods为删除状态
        pcGoodsMapper.batchSoftDelGoods(id);



        //4.设置template_spec_item为删除状态

        //获取模板id
        Long templateId = pcBaseMapper.queryCommodity(id).getTemplateId();

        //批量逻辑删除
        templateSpecItemMapper.softDelTemplateSpecValue(templateId);


        //5.设置template为删除状态
        templateMapper.softDelTemplate(templateId);

        //6.设置该基础商品为删除状态
        pcBaseMapper.softDelCommodity(id);





        return Result.ok();
    }


    // 根据id查询商品
    @Override
    public Result queryCommodity(Long id) {


        PCBaseDTO pcBaseDTO = pcBaseMapper.queryCommodity(id);

        //判断是否为空
        if (pcBaseDTO == null) {
            return Result.error("商品不存在");
        }




        return Result.ok(pcBaseDTO);
    }


}
