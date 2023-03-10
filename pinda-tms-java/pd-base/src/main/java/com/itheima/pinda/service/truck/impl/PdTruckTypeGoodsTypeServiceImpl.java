package com.itheima.pinda.service.truck.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.mapper.truck.PdTruckTypeGoodsTypeMapper;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;
import com.itheima.pinda.service.truck.IPdTruckTypeGoodsTypeService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 车辆类型与货物类型关联表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTruckTypeGoodsTypeServiceImpl extends ServiceImpl<PdTruckTypeGoodsTypeMapper, PdTruckTypeGoodsType>
        implements IPdTruckTypeGoodsTypeService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public void saveTruckTypeGoodsType(PdTruckTypeGoodsType pdTruckTypeGoodsType) {
        pdTruckTypeGoodsType.setId(idGenerator.nextId(pdTruckTypeGoodsType) + "");
        baseMapper.insert(pdTruckTypeGoodsType);
    }

    @Override
    public void batchSave(List<PdTruckTypeGoodsType> truckTypeGoodsTypeList) {
        truckTypeGoodsTypeList.forEach(pdTruckTypeGoodsType -> pdTruckTypeGoodsType.setId(idGenerator.nextId(pdTruckTypeGoodsType) + ""));
        saveBatch(truckTypeGoodsTypeList);
    }

    @Override
    public void delete(String truckTypeId, String goodsTypeId) {
        LambdaQueryWrapper<PdTruckTypeGoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        boolean canExecute = false;
        if (StringUtils.isNotEmpty(truckTypeId)) {
            lambdaQueryWrapper.eq(PdTruckTypeGoodsType::getTruckTypeId, truckTypeId);
            canExecute = true;
        }
        if (StringUtils.isNotEmpty(goodsTypeId)) {
            lambdaQueryWrapper.eq(PdTruckTypeGoodsType::getGoodsTypeId, goodsTypeId);
            canExecute = true;
        }
        if (canExecute) {
            baseMapper.delete(lambdaQueryWrapper);
        }
    }

    @Override
    public List<PdTruckTypeGoodsType> findAll(String truckTypeId, String goodsTypeId) {
        LambdaQueryWrapper<PdTruckTypeGoodsType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(truckTypeId)) {
            lambdaQueryWrapper.eq(PdTruckTypeGoodsType::getTruckTypeId, truckTypeId);
        }
        if (StringUtils.isNotEmpty(goodsTypeId)) {
            lambdaQueryWrapper.eq(PdTruckTypeGoodsType::getGoodsTypeId, goodsTypeId);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }

}
