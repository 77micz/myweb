package com.itcast.myweb.common.pojo;


import com.itcast.myweb.common.Constant;
import lombok.Data;


//分页查询参数
@Data
public class PageSearch {

    /**
     * 页码
     */
    private Long pageNo = Constant.DEFAULT_PAGE_NO;


    /**
     * 每页数量
     */
//    private final Long pageSize = Constant.ITEM_PAGE_SIZE;
    private Long pageSize;


}
