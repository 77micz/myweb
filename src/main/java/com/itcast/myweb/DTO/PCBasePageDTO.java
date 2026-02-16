package com.itcast.myweb.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PCBasePageDTO {//PC基础商品分页DTO


    private Long total;//总数

    List<PCBaseDTO> list;//商品列表


}
