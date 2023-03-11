package com.itheima.pinda.service.truck.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;
import com.itheima.pinda.mapper.truck.PdTruckTypeGoodsTypeMapper;
import com.itheima.pinda.service.truck.IPdTruckTypeGoodsTypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IPdTruckTypeGoodsTypeServiceImpl extends ServiceImpl<PdTruckTypeGoodsTypeMapper, PdTruckTypeGoodsType> implements IPdTruckTypeGoodsTypeService {

    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public void saveTruckTypeGoodsType(PdTruckTypeGoodsType pdTruckTypeGoodsType) {

    }

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

    /**
     * 根据车辆类型或者货物类型查询关联信息
     * @param truckTypeId
     * @param goodsTypeId
     * @return
     */
    @Override
    public List<PdTruckTypeGoodsType> findAll(String truckTypeId, String goodsTypeId) {
//        封装条件对象
        LambdaQueryWrapper<PdTruckTypeGoodsType> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(truckTypeId)) {
            wrapper.eq(PdTruckTypeGoodsType::getTruckTypeId, truckTypeId);
        }

        if (StringUtils.isNotEmpty(goodsTypeId)) {
            wrapper.eq(PdTruckTypeGoodsType::getGoodsTypeId, goodsTypeId);
        }


        return baseMapper.selectList(wrapper);
    }

    /**
     * 删除关联关系
     *
     * @param truckTypeId 车辆类型id
     * @param goodsTypeId 货物类型id
     */
    @Override
    public void delete(String truckTypeId, String goodsTypeId) {
        LambdaQueryWrapper<PdTruckTypeGoodsType> wrapper = new LambdaQueryWrapper<>();
        boolean canExecute = false;
        if (StringUtils.isNotEmpty(truckTypeId)) {
            wrapper.eq(PdTruckTypeGoodsType::getTruckTypeId, truckTypeId);
            canExecute = true;
        }
        if (StringUtils.isNotEmpty(goodsTypeId)) {
            wrapper.eq(PdTruckTypeGoodsType::getGoodsTypeId, goodsTypeId);
            canExecute = true;
        }
        if (canExecute) {
            baseMapper.delete(wrapper);
        }
    }
}
