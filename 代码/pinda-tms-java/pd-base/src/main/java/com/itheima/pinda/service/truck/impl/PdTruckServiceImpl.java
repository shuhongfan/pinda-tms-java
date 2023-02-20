package com.itheima.pinda.service.truck.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.mapper.truck.PdTruckMapper;
import com.itheima.pinda.entity.truck.PdTruck;
import com.itheima.pinda.service.truck.IPdTruckService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * <p>
 * 车辆信息表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTruckServiceImpl extends ServiceImpl<PdTruckMapper, PdTruck> implements IPdTruckService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public PdTruck saveTruck(PdTruck pdTruck) {
        pdTruck.setId(idGenerator.nextId(pdTruck) + "");
        baseMapper.insert(pdTruck);
        return pdTruck;
    }

    @Override
    public IPage<PdTruck> findByPage(Integer page, Integer pageSize, String truckTypeId, String licensePlate, String fleetId) {
        Page<PdTruck> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<PdTruck> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(licensePlate)) {
            lambdaQueryWrapper.like(PdTruck::getLicensePlate, licensePlate);
        }
        if (StringUtils.isNotEmpty(truckTypeId)) {
            lambdaQueryWrapper.eq(PdTruck::getTruckTypeId, truckTypeId);

        }
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(PdTruck::getFleetId, fleetId);

        }
        lambdaQueryWrapper.eq(PdTruck::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, PdTruck::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<PdTruck> findAll(List<String> ids, String fleetId) {
        LambdaQueryWrapper<PdTruck> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(PdTruck::getId, ids);
        }
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(PdTruck::getFleetId, fleetId);
        }
        lambdaQueryWrapper.eq(PdTruck::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, false, PdTruck::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public Integer count(String fleetId) {
        LambdaQueryWrapper<PdTruck> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(PdTruck::getId, fleetId);
        }
        lambdaQueryWrapper.eq(PdTruck::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectCount(lambdaQueryWrapper);
    }

    @Override
    public void disableById(String id) {
        PdTruck pdTruck = new PdTruck();
        pdTruck.setId(id);
        pdTruck.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(pdTruck);
    }

}
