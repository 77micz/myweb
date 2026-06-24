package com.itcast.myweb.domain.dto;


import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserDTO {

    private Long id;

    private String nickName;//昵称

    private String image;//头像

    private Long addressId;// 默认地址id


}
