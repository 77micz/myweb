package com.itcast.myweb.DTO;


import lombok.Data;

import java.util.List;

@Data
public class TemplateItemDTO {


    private Long id;

    private Long specTemplateId;//模板id

    private List<Long> specItemIds;//规格项id


}
