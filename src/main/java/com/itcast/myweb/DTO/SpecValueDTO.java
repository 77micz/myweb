package com.itcast.myweb.DTO;


import lombok.Data;

@Data
public class SpecValueDTO {// 规格值DTO

    private Long id;// id

    private Long templateId;// 规格模板id

    private Long specItemId;// 规格项id

    private String specValue;// 规格值

    private Integer sort;// 展示顺序


}
