package com.itcast.myweb.DTO;


import lombok.Data;

@Data
public class SpecTemplateDTO {// 规格模板DTO


    private String templateName;// 名称

    private Long categoryId;// 分类id

    private Long baseId;// 基础商品id


}
