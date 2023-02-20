package com.itheima.pinda.service.agency.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.mapper.agency.PdFleetMapper;
import com.itheima.pinda.entity.agency.PdFleet;
import com.itheima.pinda.service.agency.IPdFleetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 车队表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdFleetServiceImpl extends ServiceImpl<PdFleetMapper, PdFleet> implements IPdFleetService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public PdFleet saveFleet(PdFleet fleet) {
        fleet.setId(idGenerator.nextId(fleet) + "");
        baseMapper.insert(fleet);
        return fleet;
    }

    @Override
    public IPage<PdFleet> findByPage(Integer page, Integer pageSize, String name, String fleetNumber, String manager) {
        Page<PdFleet> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<PdFleet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.like(PdFleet::getName, name);
        }
        if (StringUtils.isNotEmpty(fleetNumber)) {
            lambdaQueryWrapper.like(PdFleet::getFleetNumber, fleetNumber);
        }
        if (StringUtils.isNotEmpty(manager)) {
            lambdaQueryWrapper.eq(PdFleet::getManager, manager);
        }
        lambdaQueryWrapper.eq(PdFleet::getStatus, Constant.DATA_DEFAULT_STATUS);
        lambdaQueryWrapper.orderBy(true, true, PdFleet::getId);
        return baseMapper.selectPage(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<PdFleet> findAll(List<String> ids, String agencyId) {
        LambdaQueryWrapper<PdFleet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(PdFleet::getId, ids);
        }
        if (StringUtils.isNotEmpty(agencyId)) {
            lambdaQueryWrapper.eq(PdFleet::getAgencyId, agencyId);
        }
        lambdaQueryWrapper.orderBy(true, true, PdFleet::getId);
        lambdaQueryWrapper.eq(PdFleet::getStatus, Constant.DATA_DEFAULT_STATUS);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void disableById(String id) {
        PdFleet fleet = new PdFleet();
        fleet.setId(id);
        fleet.setStatus(Constant.DATA_DISABLE_STATUS);
        baseMapper.updateById(fleet);
    }

}
