package com.itheima.pinda.controller;

import com.itheima.pinda.DTO.angency.FleetDto;
import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.DTO.transportline.TransportLineTypeDto;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.DTO.truck.TruckTypeDto;
import com.itheima.pinda.DTO.user.TruckDriverDto;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.enumeration.common.StaticStation;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.feign.agency.FleetFeign;
import com.itheima.pinda.feign.common.GoodsTypeFeign;
import com.itheima.pinda.feign.transportline.TransportLineTypeFeign;
import com.itheima.pinda.feign.truck.TruckFeign;
import com.itheima.pinda.feign.truck.TruckTypeFeign;
import com.itheima.pinda.feign.user.DriverFeign;
import com.itheima.pinda.util.BeanUtil;
import com.itheima.pinda.vo.base.AreaSimpleVo;
import com.itheima.pinda.vo.base.businessHall.GoodsTypeVo;
import com.itheima.pinda.vo.base.transforCenter.business.FleetVo;
import com.itheima.pinda.vo.base.transforCenter.business.TransportLineTypeVo;
import com.itheima.pinda.vo.base.transforCenter.business.TruckTypeVo;
import com.itheima.pinda.vo.base.transforCenter.business.TruckVo;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("common")
@Api(tags = "公共信息")
@Log
public class CommonController {
    @Autowired
    private AreaApi areaApi;
    @Autowired
    private FleetFeign fleetFeign;
    @Autowired
    private TruckTypeFeign truckTypeFeign;
    @Autowired
    private TransportLineTypeFeign transportLineTypeFeign;
    @Autowired
    private GoodsTypeFeign goodsTypeFeign;
    @Autowired
    private DriverFeign driverFeign;
    @Autowired
    private UserApi userApi;
    @Autowired
    private TruckFeign truckFeign;

    @ApiOperation(value = "获取行政区域简要信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父级id，无父级为0", required = true, example = "0")
    })
    @GetMapping(value = "area/simple")
    public List<AreaSimpleVo> areaSimple(@RequestParam(value = "parentId") String parentId) {
        return areaApi.findAll(StringUtils.isEmpty(parentId) ? null : Long.valueOf(parentId), null).getData().stream().map(BeanUtil::parseArea2Vo).collect(Collectors.toList());
    }

    @ApiOperation(value = "获取负责人信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "station", value = "岗位", required = true, example = "1为员工，2为快递员，3为司机"),
            @ApiImplicitParam(name = "name", value = "用户姓名", required = true, example = "张三"),
    })
    @GetMapping(value = "user/simple")
    public List<SysUserVo> userSimple(@RequestParam(name = "station", required = false) Integer station, @RequestParam(name = "name", required = false) String name) {
        // TODO: 2020/2/18 此处需考虑是否从token上下文中获取当前用户所属机构id作为查询条件
        Long stationId = null;
        if (station != null && station == Constant.UserStation.COURIER.getStation()) {
            stationId = StaticStation.COURIER_ID;
        } else if (station != null && station == Constant.UserStation.DRIVER.getStation()) {
            stationId = StaticStation.DRIVER_ID;
        }
        R<List<User>> result = userApi.list(null, stationId, name, null);
        List<SysUserVo> userVoList = new ArrayList<>();
        if (result.getIsSuccess() && result.getData() != null) {
            userVoList.addAll(result.getData().stream().map(user -> BeanUtil.parseUser2Vo(user, null, null)).collect(Collectors.toList()));
        }
        return userVoList;
    }

    @ApiOperation(value = "获取车队信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "more", value = "是否获取更多信息，例如,司机和车辆，默认为false", required = true, example = "true")
    })
    @GetMapping(value = "fleet/simple")
    public List<FleetVo> fleetSimple(@RequestParam(name = "more", required = false, defaultValue = "false") Boolean more) {
        // TODO: 2020/2/18 此处需考虑是否从token上下文中获取当前用户所属机构id作为查询条件
        List<FleetDto> fleetDtoList = fleetFeign.findAll(null, null);
        return fleetDtoList.stream().map(fleetDto -> {
            FleetVo simpleVo = new FleetVo();
            BeanUtils.copyProperties(fleetDto, simpleVo);
            if (more) {
                List<TruckDto> truckDtoList = truckFeign.findAll(null, fleetDto.getId());
                if (truckDtoList != null && truckDtoList.size() > 0) {
                    simpleVo.setTrucks(truckDtoList.stream().map(truckDto -> {
                        TruckVo truckVo = new TruckVo();
                        BeanUtils.copyProperties(truckDto, truckVo);
                        return truckVo;
                    }).collect(Collectors.toList()));
                }
                List<TruckDriverDto> driverDtoList = driverFeign.findAllDriver(null, fleetDto.getId());
                if (driverDtoList != null && driverDtoList.size() > 0) {
                    List<SysUserVo> driverVoList = new ArrayList<>();
                    R<List<User>> userResult = userApi.list(driverDtoList.stream().mapToLong(driverDto -> Long.valueOf(driverDto.getUserId())).boxed().collect(Collectors.toList()), null, null, null);
                    if (userResult.getIsSuccess() && userResult.getData() != null && userResult.getData().size() > 0) {
                        driverVoList.addAll(userResult.getData().stream().map(user -> BeanUtil.parseUser2Vo(user, null, null)).collect(Collectors.toList()));
                    }
                    simpleVo.setDrivers(driverVoList);
                    simpleVo.setDriverCount(driverVoList.size());
                }
            }
            return simpleVo;
        }).collect(Collectors.toList());
    }

    @ApiOperation(value = "获取车辆类型信息列表")
    @GetMapping(value = "truckType/simple")
    public List<TruckTypeVo> truckTypeSimple() {
        // TODO: 2020/2/18 此处需考虑是否从token上下文中获取当前用户所属机构id作为查询条件
        List<TruckTypeDto> truckTypeDtoList = truckTypeFeign.findAll(null);
        return truckTypeDtoList.stream().map(truckTypeDto -> {
            TruckTypeVo simpleVo = new TruckTypeVo();
            BeanUtils.copyProperties(truckTypeDto, simpleVo);
            return simpleVo;
        }).collect(Collectors.toList());
    }

    @ApiOperation(value = "获取线路类型信息列表")
    @GetMapping(value = "transportLineType/simple")
    public List<TransportLineTypeVo> transportLineTypeSimple() {
        // TODO: 2020/2/18 此处需考虑是否从token上下文中获取当前用户所属机构id作为查询条件
        List<TransportLineTypeDto> transportLineTypeDtoList = transportLineTypeFeign.findAll(null);
        return transportLineTypeDtoList.stream().map(transportLineTypeDto -> {
            TransportLineTypeVo simpleVo = new TransportLineTypeVo();
            BeanUtils.copyProperties(transportLineTypeDto, simpleVo);
            return simpleVo;
        }).collect(Collectors.toList());
    }

    @ApiOperation(value = "获取货物类型信息列表")
    @GetMapping(value = "goodsType/simple")
    public List<GoodsTypeVo> goodsTypeSimple() {
        List<GoodsTypeDto> goodsTypeDtoList = goodsTypeFeign.findAll(null);
        return goodsTypeDtoList.stream().map(goodsTypeDto -> {
            GoodsTypeVo simpleVo = new GoodsTypeVo();
            BeanUtils.copyProperties(goodsTypeDto, simpleVo);
            return simpleVo;
        }).collect(Collectors.toList());
    }
}
