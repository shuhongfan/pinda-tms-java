package com.itheima.pinda.service.truck.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;
import com.itheima.pinda.mapper.truck.PdTruckTypeGoodsTypeMapper;
import com.itheima.pinda.service.truck.IPdTruckTypeGoodsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPdTruckTypeGoodsTypeServiceImpl extends ServiceImpl<PdTruckTypeGoodsTypeMapper, PdTruckTypeGoodsType> implements IPdTruckTypeGoodsTypeService {

    @Autowired
    private CustomIdGenerator idGenerator;

    /**
     * 批量添加车辆类型与货物类型关联
     * @param pdTruckTypeGoodsTypes
     */
    @Override
    public void batchSave(List<PdTruckTypeGoodsType> pdTruckTypeGoodsTypes) {
        pdTruckTypeGoodsTypes.forEach(pdTruckTypeGoodsType -> {
            pdTruckTypeGoodsType.setId(String.valueOf(idGenerator.nextId(pdTruckTypeGoodsType)));
        });

        saveBatch(pdTruckTypeGoodsTypes);
    }
}
