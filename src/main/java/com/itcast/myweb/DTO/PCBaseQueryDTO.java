package com.itcast.myweb.DTO;


import lombok.Data;

@Data
public class PCBaseQueryDTO {//PC基础商品查询DTO


    private Integer pageNum=1;//页码

    private Integer pageSize=10;//每页记录数


    private String name;//商品名称


}
