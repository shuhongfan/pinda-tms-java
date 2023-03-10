package com.itheima.pinda.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.mapper.user.PdTruckDriverLicenseMapper;
import com.itheima.pinda.entity.user.PdTruckDriverLicense;
import com.itheima.pinda.service.user.IPdTruckDriverLicenseService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 司机驾驶证表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTruckDriverLicenseServiceImpl extends ServiceImpl<PdTruckDriverLicenseMapper, PdTruckDriverLicense> implements IPdTruckDriverLicenseService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public PdTruckDriverLicense saveTruckDriverLicense(PdTruckDriverLicense pdTruckDriverLicense) {
        PdTruckDriverLicense driverLicense = baseMapper.selectOne(new LambdaQueryWrapper<PdTruckDriverLicense>().eq(PdTruckDriverLicense::getUserId, pdTruckDriverLicense.getUserId()));
        if (driverLicense == null) {
            pdTruckDriverLicense.setId(idGenerator.nextId(pdTruckDriverLicense) + "");
        } else {
            pdTruckDriverLicense.setId(driverLicense.getId());
        }
        saveOrUpdate(pdTruckDriverLicense);
        return pdTruckDriverLicense;
    }

    @Override
    public PdTruckDriverLicense findOne(String userId) {
        LambdaQueryWrapper<PdTruckDriverLicense> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(PdTruckDriverLicense::getUserId, userId);
        }
        return getOne(lambdaQueryWrapper);
    }

}
