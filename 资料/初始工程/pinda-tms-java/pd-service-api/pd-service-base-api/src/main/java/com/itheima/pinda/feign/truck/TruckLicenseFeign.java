package com.itheima.pinda.feign.truck;

import com.itheima.pinda.DTO.truck.TruckLicenseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient(name = "pd-base")
@RequestMapping("base/truck/license")
@ApiIgnore
public interface TruckLicenseFeign {
    /**
     * 保存车辆行驶证信息
     *
     * @param dto 车辆行驶证信息
     * @return 车辆行驶证信息
     */
    @PostMapping("")
    TruckLicenseDto saveTruckLicense(@RequestBody TruckLicenseDto dto);

    /**
     * 根据id获取车辆行驶证详情
     *
     * @param id 车辆行驶证id
     * @return 车辆行驶证信息
     */
    @GetMapping("/{id}")
    TruckLicenseDto fineById(@PathVariable(name = "id") String id);
}
