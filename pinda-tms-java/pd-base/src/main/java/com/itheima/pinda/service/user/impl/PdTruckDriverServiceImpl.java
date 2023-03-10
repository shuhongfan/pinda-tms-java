package com.itheima.pinda.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.mapper.user.PdTruckDriverMapper;
import com.itheima.pinda.entity.user.PdTruckDriver;
import com.itheima.pinda.service.user.IPdTruckDriverService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 司机表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTruckDriverServiceImpl extends ServiceImpl<PdTruckDriverMapper, PdTruckDriver>
        implements IPdTruckDriverService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public PdTruckDriver saveTruckDriver(PdTruckDriver pdTruckDriver) {
        PdTruckDriver driver = baseMapper.selectOne(new LambdaQueryWrapper<PdTruckDriver>().eq(PdTruckDriver::getUserId, pdTruckDriver.getUserId()));
        if (driver == null) {
            pdTruckDriver.setId(idGenerator.nextId(pdTruckDriver) + "");
        } else {
            pdTruckDriver.setId(driver.getId());
        }
        saveOrUpdate(pdTruckDriver);
        return pdTruckDriver;
    }

    @Override
    public List<PdTruckDriver> findAll(List<String> userIds, String fleetId) {
        boolean hasUserIds = userIds != null && userIds.size() > 0;
        boolean hasFleetId = StringUtils.isNotEmpty(fleetId);
        if (!hasUserIds && !hasFleetId) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<PdTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (hasUserIds) {
            lambdaQueryWrapper.in(PdTruckDriver::getUserId, userIds);
        }
        if (hasFleetId) {
            lambdaQueryWrapper.eq(PdTruckDriver::getFleetId, fleetId);
        }
        lambdaQueryWrapper.orderBy(true, false, PdTruckDriver::getId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public PdTruckDriver findOne(String userId) {
        LambdaQueryWrapper<PdTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(PdTruckDriver::getUserId, userId);
        }
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public Integer count(String fleetId) {
        LambdaQueryWrapper<PdTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(PdTruckDriver::getFleetId, fleetId);
        }
        return count(lambdaQueryWrapper);
    }

    @Override
    public IPage<PdTruckDriver> findByPage(Integer page, Integer pageSize, String fleetId) {
        Page<PdTruckDriver> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<PdTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(fleetId)) {
            lambdaQueryWrapper.eq(PdTruckDriver::getFleetId, fleetId);
        }
        lambdaQueryWrapper.orderBy(true, false, PdTruckDriver::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }
}
