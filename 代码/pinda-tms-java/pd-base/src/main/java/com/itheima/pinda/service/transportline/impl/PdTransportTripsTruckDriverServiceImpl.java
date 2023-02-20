package com.itheima.pinda.service.transportline.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.transportline.PdTransportTripsTruckDriver;
import com.itheima.pinda.mapper.transportline.PdTransportTripsTruckDriverMapper;
import com.itheima.pinda.service.transportline.IPdTransportTripsTruckDriverService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 车次与车辆关联信息表 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Service
public class PdTransportTripsTruckDriverServiceImpl extends ServiceImpl<PdTransportTripsTruckDriverMapper, PdTransportTripsTruckDriver>
        implements IPdTransportTripsTruckDriverService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public void batchSave(String truckTransportTripsId, List<PdTransportTripsTruckDriver> truckTransportTripsTruckDriverList) {
        LambdaQueryWrapper<PdTransportTripsTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PdTransportTripsTruckDriver::getTransportTripsId, truckTransportTripsId);
        //查出操作前关系列表
        List<PdTransportTripsTruckDriver> transportTripsTruckDriverList = baseMapper.selectList(lambdaQueryWrapper);
        Map<String, PdTransportTripsTruckDriver> sourceTruckKeyMap = new HashMap<>();
        for (PdTransportTripsTruckDriver pdTransportTripsTruckDriver:transportTripsTruckDriverList){
            sourceTruckKeyMap.put(pdTransportTripsTruckDriver.getTransportTripsId() + "_" + pdTransportTripsTruckDriver.getTruckId(),pdTransportTripsTruckDriver);
        }
        //清除关系
        baseMapper.delete(lambdaQueryWrapper);
        List<PdTransportTripsTruckDriver> saveList = new ArrayList<>();
        //遍历传入数据
        truckTransportTripsTruckDriverList.forEach(pdTransportTripsTruckDriver -> {
            PdTransportTripsTruckDriver saveData = new PdTransportTripsTruckDriver();
            BeanUtils.copyProperties(pdTransportTripsTruckDriver, saveData);
            saveData.setId(idGenerator.nextId(saveData) + "");
            if (sourceTruckKeyMap.containsKey(pdTransportTripsTruckDriver.getTransportTripsId() + "_" + pdTransportTripsTruckDriver.getTruckId())) {
                PdTransportTripsTruckDriver source = sourceTruckKeyMap.get(pdTransportTripsTruckDriver.getTransportTripsId() + "_" + pdTransportTripsTruckDriver.getTruckId());
                if (saveData.getUserId() == null) {
                    saveData.setUserId(source.getUserId());
                }
            }
            saveList.add(saveData);
        });
        saveBatch(saveList);
    }

    @Override
    public List<PdTransportTripsTruckDriver> findAll(String transportTripsId, String truckId, String userId) {
        LambdaQueryWrapper<PdTransportTripsTruckDriver> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(transportTripsId)) {
            lambdaQueryWrapper.eq(PdTransportTripsTruckDriver::getTransportTripsId, transportTripsId);
        }
        if (StringUtils.isNotEmpty(truckId)) {
            lambdaQueryWrapper.eq(PdTransportTripsTruckDriver::getTruckId, truckId);
        }
        if (StringUtils.isNotEmpty(userId)) {
            lambdaQueryWrapper.eq(PdTransportTripsTruckDriver::getUserId, userId);
        }
        return baseMapper.selectList(lambdaQueryWrapper);
    }
}
