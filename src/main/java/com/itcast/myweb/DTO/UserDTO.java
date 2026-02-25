package com.itcast.myweb.DTO;


import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserDTO {

    private Long id;

    private String nickName;//昵称

    private String image;//头像






}
