package com.itheima.pinda.DTO;

import com.itheima.pinda.entity.CacheLineEntity;
import lombok.Data;

import java.util.List;

@Data
public class CacheLineDTO extends CacheLineEntity {

    private List<CacheLineDetailDTO> cacheLineDetailDTOS;

}
