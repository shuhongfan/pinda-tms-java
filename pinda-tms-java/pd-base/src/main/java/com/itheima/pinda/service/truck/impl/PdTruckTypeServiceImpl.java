package com.itheima.pinda.service.truck.impl;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.mapper.truck.PdTruckTypeMapper;
import com.itheima.pinda.entity.truck.PdTruckType;
import com.itheima.pinda.service.truck.IPdTruckTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 车辆类型表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTruckTypeServiceImpl extends ServiceImpl<PdTruckTypeMapper, PdTruckType> implements IPdTruckTypeService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public PdTruckType saveTruckType(PdTruckType pdTruckType) {
        pdTruckType.setId(idGenerator.nextId(pdTruckType) + "");
        baseMapper.insert(pdTruckType);
        return pdTruckType;
    }

    @Override
    public IPage<PdTruckType> findByPage(Integer page, Integer pageSize, String name, BigDecimal allowableLoad,
                                         BigDecimal allowableVolume) {
        Page<PdTruckType> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<PdTruckType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(PdTruckType::getName, name);
        }
        if (allowableLoad != null) {
            lambdaQueryWrapper.eq(PdTruckType::getAllowableLoad, allowableLoad);
        }
        if (allowableVolume != null) {
            lambdaQueryWrapper.eq(PdTruckType::getAllowableVolume, allowableVolume);
        }
        lambdaQueryWrapper.eq(PdTruckType::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<PdTruckType> findAll(List<String> ids) {
        LambdaQueryWrapper<PdTruckType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(PdTruckType::getId, ids);
        }
        lambdaQueryWrapper.eq(PdTruckType::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
