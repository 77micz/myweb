package com.itcast.myweb.domain.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemDetailVO extends ItemBaseVO {


    // sku列表
    private List<ItemSkuVO> skuVOList;




}
