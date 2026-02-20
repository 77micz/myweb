package com.itcast.myweb.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GoodsSpecValueDTO {


    private Long id;


    private Long goodsId;//商品id


    private List<Long> specValueIds;//规格值id


}
