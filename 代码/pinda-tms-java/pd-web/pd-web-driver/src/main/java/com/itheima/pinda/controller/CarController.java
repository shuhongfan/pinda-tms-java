package com.itheima.pinda.controller;


import com.itheima.pinda.DTO.CarInfoDTO;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.DTO.truck.TruckLicenseDto;
import com.itheima.pinda.DTO.truck.TruckTypeDto;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.feign.truck.TruckFeign;
import com.itheima.pinda.feign.truck.TruckLicenseFeign;
import com.itheima.pinda.feign.truck.TruckTypeFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 运单表 前端控制器
 * </p>
 *
 * @author diesel
 * @since 2020-03-19
 */
@Slf4j
@Api(tags = "车辆信息")
@Controller
@RequestMapping("business/car")
public class CarController {

    private final TruckFeign truckFeign;

    private final TruckTypeFeign truckTypeFeign;

    private final TruckLicenseFeign truckLicenseFeign;

    public CarController(TruckFeign truckFeign, TruckTypeFeign truckTypeFeign, TruckLicenseFeign truckLicenseFeign) {
        this.truckFeign = truckFeign;
        this.truckTypeFeign = truckTypeFeign;
        this.truckLicenseFeign = truckLicenseFeign;
    }

    @SneakyThrows
    @ApiOperation(value = "获取车辆信息")
    @ApiImplicitParam(name = "id", value = "主键", required = true)
    @ResponseBody
    @GetMapping("info")
    public Result info(String id) {
        log.info("获取车辆信息：{}", id);
        TruckDto truckDto = truckFeign.fineById(id);
        log.info("获取车辆信息：{}", truckDto);
        TruckTypeDto truckTypeDto = truckTypeFeign.fineById(truckDto.getTruckTypeId());
        log.info("获取车辆信息 车辆类型：{}", truckTypeDto);

        TruckLicenseDto truckLicenseDto = null;
        if (StringUtils.isNotEmpty(truckDto.getTruckLicenseId())) {
            truckLicenseDto = truckLicenseFeign.fineById(truckDto.getTruckLicenseId());
            log.info("获取车辆信息 车辆行驶证信息：{}", truckLicenseDto);
        }

        return Result.ok().put("data", CarInfoDTO.builder()
                .brand(truckDto.getBrand())
                .carModel(truckTypeDto.getName())
                .carSize(truckTypeDto.getMeasureLong() + "*" + truckTypeDto.getMeasureWidth() + "*" + truckTypeDto.getMeasureHigh())
                .id(truckDto.getId())
                .licensePlate(truckDto.getLicensePlate())
                .load(truckDto.getAllowableLoad() == null ? "" : truckDto.getAllowableLoad().toString())
                .picture(truckLicenseDto != null ? truckLicenseDto.getPicture() : "").build());
    }
}
