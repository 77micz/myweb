package com.itcast.myweb.DTO;

import lombok.Data;

@Data
public class ItemDTO {

    private Integer id;

    private String image;// 商品图片

    private String title;// 商品标题

    private Double price;// 商品价格

    private Integer sales;// 销售量

//    -----------

    private Long categoryId;// 商品分类id

}
