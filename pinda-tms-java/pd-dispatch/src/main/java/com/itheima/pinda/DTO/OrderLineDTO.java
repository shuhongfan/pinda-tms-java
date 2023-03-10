package com.itheima.pinda.DTO;

import com.itheima.pinda.entity.CacheLineDetailEntity;
import com.itheima.pinda.entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class OrderLineDTO {

    private CacheLineDetailEntity cacheLineDetailEntity;

    private OrderClassifyGroupDTO orderClassifyGroupDTO;

    public OrderLineDTO(CacheLineDetailEntity cacheLineDetailEntity,OrderClassifyGroupDTO orderClassifyGroupDTO) {
        this.cacheLineDetailEntity = cacheLineDetailEntity;
        this.orderClassifyGroupDTO = orderClassifyGroupDTO;
    }
}
