package com.itheima.pinda.controller.truck;

import com.itheima.pinda.entity.truck.PdTruck;
import com.itheima.pinda.entity.truck.PdTruckLicense;
import com.itheima.pinda.service.truck.IPdTruckLicenseService;
import com.itheima.pinda.DTO.truck.TruckLicenseDto;

import com.itheima.pinda.service.truck.IPdTruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.BeanUtils;

/**
 * TruckLicenseController
 */
@RestController
@RequestMapping("base/truck/license")
public class TruckLicenseController {
    @Autowired
    private IPdTruckLicenseService truckLicenseService;
    @Autowired
    private IPdTruckService truckService;

    /**
     * 保存车辆行驶证信息
     *
     * @param dto 车辆行驶证信息
     * @return 车辆行驶证信息
     */
    @PostMapping("")
    public TruckLicenseDto saveTruckLicense(@RequestBody TruckLicenseDto dto) {
        PdTruckLicense pdTruckLicense = new PdTruckLicense();
        BeanUtils.copyProperties(dto, pdTruckLicense);
        pdTruckLicense = truckLicenseService.saveTruckLicense(pdTruckLicense);
        if (dto.getId() == null) {
            PdTruck truck = new PdTruck();
            truck.setId(dto.getId());
            truck.setTruckLicenseId(pdTruckLicense.getId());
            truckService.saveTruck(truck);
        }
        BeanUtils.copyProperties(pdTruckLicense, dto);
        return dto;
    }

    /**
     * 根据id获取车辆行驶证详情
     *
     * @param id 车辆行驶证id
     * @return 车辆行驶证信息
     */
    @GetMapping("/{id}")
    public TruckLicenseDto fineById(@PathVariable(name = "id") String id) {
        PdTruckLicense pdTruckLicense = truckLicenseService.getById(id);
        TruckLicenseDto dto = new TruckLicenseDto();
        BeanUtils.copyProperties(pdTruckLicense, dto);
        return dto;
    }
}