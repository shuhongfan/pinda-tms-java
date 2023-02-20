package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.pinda.DTO.transportline.TransportLineDto;
import com.itheima.pinda.DTO.transportline.TransportLineTypeDto;
import com.itheima.pinda.DTO.transportline.TransportTripsDto;
import com.itheima.pinda.DTO.transportline.TransportTripsTruckDriverDto;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.enumeration.common.StaticStation;
import com.itheima.pinda.authority.enumeration.core.OrgType;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.angency.FleetDto;
import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.DTO.truck.TruckLicenseDto;
import com.itheima.pinda.DTO.truck.TruckTypeDto;
import com.itheima.pinda.DTO.user.TruckDriverDto;
import com.itheima.pinda.DTO.user.TruckDriverLicenseDto;
import com.itheima.pinda.feign.agency.FleetFeign;
import com.itheima.pinda.feign.common.GoodsTypeFeign;
import com.itheima.pinda.feign.transportline.TransportLineFeign;
import com.itheima.pinda.feign.transportline.TransportLineTypeFeign;
import com.itheima.pinda.feign.transportline.TransportTripsFeign;
import com.itheima.pinda.feign.truck.TruckFeign;
import com.itheima.pinda.feign.truck.TruckLicenseFeign;
import com.itheima.pinda.feign.truck.TruckTypeFeign;
import com.itheima.pinda.feign.user.DriverFeign;
import com.itheima.pinda.future.PdCompletableFuture;
import com.itheima.pinda.util.BeanUtil;
import com.itheima.pinda.vo.base.angency.AgencySimpleVo;
import com.itheima.pinda.vo.base.angency.AgencyVo;
import com.itheima.pinda.vo.base.businessHall.GoodsTypeVo;
import com.itheima.pinda.vo.base.transforCenter.business.*;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 转运中心管理-业务信息管理
 */
@RestController
@RequestMapping("transfor-center/bussiness")
@Api(tags = "转运中心管理-业务信息管理")
@Log
public class TransforCenterBusinessController {
    @Autowired
    private TruckTypeFeign truckTypeFeign;
    @Autowired
    private GoodsTypeFeign goodsTypeFeign;
    @Autowired
    private TransportLineTypeFeign transportLineTypeFeign;
    @Autowired
    private FleetFeign fleetFeign;
    @Autowired
    private OrgApi orgApi;
    @Autowired
    private TruckFeign truckFeign;
    @Autowired
    private TruckLicenseFeign truckLicenseFeign;
    @Autowired
    private TransportLineFeign transportLineFeign;
    @Autowired
    private TransportTripsFeign transportTripsFeign;
    @Autowired
    private UserApi userApi;
    @Autowired
    private DriverFeign driverFeign;

    @ApiOperation(value = "添加车辆类型")
    @PostMapping("/truckType")
    public TruckTypeVo saveTruckType(@RequestBody TruckTypeVo vo) {
        TruckTypeDto dto = new TruckTypeDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getGoodsTypes() != null) {
            dto.setGoodsTypeIds(vo.getGoodsTypes().stream().map(goodsTypeVo -> goodsTypeVo.getId()).collect(Collectors.toList()));
        }
        TruckTypeDto resultDto = truckTypeFeign.saveTruckType(dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "更新车辆类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆类型id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/truckType/{id}")
    public TruckTypeVo updateTruckType(@PathVariable(name = "id") String id, @RequestBody TruckTypeVo vo) {
        vo.setId(id);
        TruckTypeDto dto = new TruckTypeDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getGoodsTypes() != null) {
            dto.setGoodsTypeIds(vo.getGoodsTypes().stream().map(goodsTypeVo -> goodsTypeVo.getId()).collect(Collectors.toList()));
        }
        TruckTypeDto resultDto = truckTypeFeign.update(id, dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取车辆类型分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, example = "10"),
            @ApiImplicitParam(name = "name", value = "车辆类型名称"),
            @ApiImplicitParam(name = "allowableLoad", value = "车型载重"),
            @ApiImplicitParam(name = "allowableVolume", value = "车型体积")
    })
    @GetMapping("/truckType/page")
    public PageResponse<TruckTypeVo> findTruckTypeByPage(@RequestParam(name = "page") Integer page,
                                                         @RequestParam(name = "pageSize") Integer pageSize,
                                                         @RequestParam(name = "name", required = false) String name,
                                                         @RequestParam(name = "allowableLoad", required = false) BigDecimal allowableLoad,
                                                         @RequestParam(name = "allowableVolume", required = false) BigDecimal allowableVolume) {
        // TODO: 2020/1/8 载重与体积查询条件，是否使用基于该值的上下区间浮动查询
        PageResponse<TruckTypeDto> truckTypeDtoPage = truckTypeFeign.findByPage(page, pageSize, name, allowableLoad, allowableVolume);
        Set<String> goodsTypeSet = new HashSet<>();
        List<TruckTypeDto> truckTypeDtoList = truckTypeDtoPage.getItems();
        truckTypeDtoList.forEach(dto -> {
            if (dto.getGoodsTypeIds() != null) {
                goodsTypeSet.addAll(dto.getGoodsTypeIds());
            }
        });
        //处理货物类型数据
        CompletableFuture<Map> goodsTypeFuture = null;
        if (goodsTypeSet.size() > 0) {
            goodsTypeFuture = PdCompletableFuture.goodsTypeMapFuture(goodsTypeFeign, goodsTypeSet);
            CompletableFuture.allOf(goodsTypeFuture).join();
        }
        CompletableFuture<Map> finalGoodsTypeFuture = goodsTypeFuture;
        List<TruckTypeVo> truckTypeVoList = truckTypeDtoList.stream().map(dto -> {
            TruckTypeVo vo = new TruckTypeVo();
            BeanUtils.copyProperties(dto, vo);
            try {
                if (dto.getGoodsTypeIds() != null) {
                    List<GoodsTypeVo> goodsTypeVoList = new ArrayList<>();
                    for (String goodsTypeId : dto.getGoodsTypeIds()) {
                        goodsTypeVoList.add(finalGoodsTypeFuture == null ? null : (GoodsTypeVo) finalGoodsTypeFuture.get().get(goodsTypeId));
                    }
                    vo.setGoodsTypes(goodsTypeVoList);
                }
            } catch (Exception e) {
                // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                e.printStackTrace();
            }
            return vo;
        }).collect(Collectors.toList());

        return PageResponse.<TruckTypeVo>builder().items(truckTypeVoList).page(page).pagesize(pageSize).pages(truckTypeDtoPage.getPages()).counts(truckTypeDtoPage.getCounts()).build();
    }

    @ApiOperation(value = "获取车辆类型详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆类型id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/truckType/{id}")
    public TruckTypeVo findTruckTypeById(@PathVariable(name = "id") String id) {
        TruckTypeDto dto = truckTypeFeign.fineById(id);
        TruckTypeVo vo = new TruckTypeVo();
        BeanUtils.copyProperties(dto, vo);
        //处理关联数据
        if (dto.getGoodsTypeIds() != null && dto.getGoodsTypeIds().size() > 0) {
            CompletableFuture<List<GoodsTypeDto>> goodsTypeFuture = PdCompletableFuture.goodsTypeListFuture(goodsTypeFeign, dto.getGoodsTypeIds());
            CompletableFuture.allOf(goodsTypeFuture);
            try {
                vo.setGoodsTypes(goodsTypeFuture.get().stream().map(goodsTypeDto -> {
                    GoodsTypeVo goodsTypeVo = new GoodsTypeVo();
                    BeanUtils.copyProperties(goodsTypeDto, goodsTypeVo);
                    return goodsTypeVo;
                }).collect(Collectors.toList()));
            } catch (Exception e) {
                // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                e.printStackTrace();
            }
        }
        return vo;
    }

    @ApiOperation(value = "删除车辆类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆类型id", required = true, example = "1", paramType = "{path}")
    })
    @DeleteMapping("/truckType/{id}")
    public Result deleteTruckType(@PathVariable(name = "id") String id) {
        // TODO: 2020/1/7 检查车辆类型与其他数据关联，存在关联不可删除，不存在关联即删除
        truckTypeFeign.disable(id);
        return Result.ok();
    }

    @ApiOperation(value = "添加线路类型")
    @PostMapping("/transportLineType")
    public TransportLineTypeVo saveTransportLineType(@RequestBody TransportLineTypeVo vo) {
        TransportLineTypeDto dto = new TransportLineTypeDto();
        BeanUtils.copyProperties(vo, dto);
        // TODO: 2020/1/8 更新人信息 从token中获取
        dto.setUpdater("1");
        TransportLineTypeDto resultDto = transportLineTypeFeign.saveTransportLineType(dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "更新线路类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "线路类型id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/transportLineType/{id}")
    public TransportLineTypeVo updateTransportLineType(@PathVariable(name = "id") String id, @RequestBody TransportLineTypeVo vo) {
        vo.setId(id);
        TransportLineTypeDto dto = new TransportLineTypeDto();
        BeanUtils.copyProperties(vo, dto);
        TransportLineTypeDto resultDto = transportLineTypeFeign.update(id, dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取线路类型分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, example = "10"),
            @ApiImplicitParam(name = "typeNumber", value = "类型编号"),
            @ApiImplicitParam(name = "name", value = "类型名称"),
            @ApiImplicitParam(name = "agencyType", value = "机构类型")
    })
    @GetMapping("/transportLineType/page")
    public PageResponse<TransportLineTypeVo> findTransportLineTypeByPage(@RequestParam(name = "page") Integer page,
                                                                         @RequestParam(name = "pageSize") Integer pageSize,
                                                                         @RequestParam(name = "typeNumber", required = false) String typeNumber,
                                                                         @RequestParam(name = "name", required = false) String name,
                                                                         @RequestParam(name = "agencyType", required = false) Integer agencyType) {
        PageResponse<TransportLineTypeDto> transportLineTypeDtoPage = transportLineTypeFeign.findByPage(page, pageSize, typeNumber, name, agencyType);
        //加工数据
        List<TransportLineTypeDto> transportLineTypeDtoList = transportLineTypeDtoPage.getItems();
        Set<String> userSet = new HashSet<>();
        transportLineTypeDtoList.forEach(transportLineTypeDto -> {
            if (transportLineTypeDto.getUpdater() != null) {
                userSet.add(transportLineTypeDto.getUpdater());
            }
        });
        CompletableFuture<Map> userFuture = PdCompletableFuture.userMapFuture(userApi, userSet, null, null, null);
        CompletableFuture.allOf(userFuture).join();
        List<TransportLineTypeVo> transportLineTypeVoList = transportLineTypeDtoList.stream().map(transportLineTypeDto -> {
            TransportLineTypeVo vo = new TransportLineTypeVo();
            BeanUtils.copyProperties(transportLineTypeDto, vo);
            if (transportLineTypeDto.getUpdater() != null) {
                try {
                    vo.setUpdater((SysUserVo) userFuture.get().get(transportLineTypeDto.getUpdater()));
                } catch (Exception e) {
                    // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                    e.printStackTrace();
                }
            }
            if (transportLineTypeDto.getLastUpdateTime() != null) {
                vo.setLastUpdateTime(transportLineTypeDto.getLastUpdateTime().format(DateTimeFormatter.ofPattern(Constant.STAND_DATE_TIME_FORMAT)));
            }
            if (transportLineTypeDto.getStartAgencyType() != null) {
                vo.setStartAgencyTypeName(OrgType.getEnumByType(transportLineTypeDto.getStartAgencyType()).getName());
            }
            if (transportLineTypeDto.getEndAgencyType() != null) {
                vo.setEndAgencyTypeName(OrgType.getEnumByType(transportLineTypeDto.getEndAgencyType()).getName());
            }
            return vo;
        }).collect(Collectors.toList());

        return PageResponse.<TransportLineTypeVo>builder().items(transportLineTypeVoList).counts(transportLineTypeDtoPage.getCounts()).page(page).pagesize(pageSize).pages(transportLineTypeDtoPage.getPages()).build();
    }

    @ApiOperation(value = "获取线路类型详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "线路类型id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/transportLineType/{id}")
    public TransportLineTypeVo findTransportLineTypeById(@PathVariable(name = "id") String id) {
        TransportLineTypeDto dto = transportLineTypeFeign.fineById(id);
        TransportLineTypeVo vo = new TransportLineTypeVo();
        BeanUtils.copyProperties(dto, vo);
        if (dto.getLastUpdateTime() != null) {
            vo.setLastUpdateTime(dto.getLastUpdateTime().format(DateTimeFormatter.ofPattern(Constant.STAND_DATE_TIME_FORMAT)));
        }
        if (dto.getStartAgencyType() != null) {
            vo.setStartAgencyTypeName(OrgType.getEnumByType(dto.getStartAgencyType()).getName());
        }
        if (dto.getEndAgencyType() != null) {
            vo.setEndAgencyTypeName(OrgType.getEnumByType(dto.getEndAgencyType()).getName());
        }
        if (dto.getUpdater() != null) {
            R<User> result = userApi.get(Long.valueOf(dto.getUpdater()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setUpdater(BeanUtil.parseUser2Vo(result.getData(), null, null));
            }
        }
        return vo;
    }

    @ApiOperation(value = "删除线路类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "线路类型id", required = true, example = "1", paramType = "{path}")
    })
    @DeleteMapping("/transportLineType/{id}")
    public Result deleteGoodsType(@PathVariable(name = "id") String id) {
        // TODO: 2020/1/8 需检查线路类型与其他数据关联，存在关联不可删除，不存在则删除
        transportLineTypeFeign.disable(id);
        return Result.ok();
    }

    @ApiOperation(value = "添加车队")
    @PostMapping("/fleet")
    public FleetVo saveFleet(@RequestBody FleetVo vo) {
        FleetDto dto = new FleetDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getManager() != null) {
            dto.setManager(vo.getManager().getUserId());
        }
        if (vo.getAgency() != null) {
            dto.setAgencyId(vo.getAgency().getId());
        }
        FleetDto resultDto = fleetFeign.saveAgencyType(dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "更新车队信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车队id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/fleet/{id}")
    public FleetVo updateFleet(@PathVariable(name = "id") String id, @RequestBody FleetVo vo) {
        vo.setId(id);
        FleetDto dto = new FleetDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getManager() != null) {
            dto.setManager(vo.getManager().getUserId());
        }
        if (vo.getAgency() != null) {
            dto.setAgencyId(vo.getAgency().getId());
        }
        FleetDto resultDto = fleetFeign.update(id, dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取车队分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, example = "10"),
            @ApiImplicitParam(name = "name", value = "车队名称"),
            @ApiImplicitParam(name = "fleetNumber", value = "车队编号"),
            @ApiImplicitParam(name = "manager", value = "负责人id")
    })
    @GetMapping("/fleet/page")
    public PageResponse<FleetVo> findFleetByPage(@RequestParam(name = "page") Integer page,
                                                 @RequestParam(name = "pageSize") Integer pageSize,
                                                 @RequestParam(name = "name", required = false) String name,
                                                 @RequestParam(name = "manager", required = false) String manager,
                                                 @RequestParam(name = "fleetNumber", required = false) String fleetNumber) {
        PageResponse<FleetDto> fleetDtoPage = fleetFeign.findByPage(page, pageSize, name, fleetNumber, manager);
        //加工数据
        List<FleetDto> fleetDtoList = fleetDtoPage.getItems();
        Set<Long> agencySet = new HashSet<>();
        Set<String> userSet = new HashSet<>();
        fleetDtoList.forEach(fleetDto -> {
            if (fleetDto.getAgencyId() != null) {
                agencySet.add(Long.valueOf(fleetDto.getAgencyId()));
            }
            if (fleetDto.getManager() != null) {
                userSet.add(fleetDto.getManager());
            }
        });
        CompletableFuture<Map> agencyFuture = PdCompletableFuture.agencyMapFuture(orgApi, agencySet);
        CompletableFuture<Map> userFuture = PdCompletableFuture.userMapFuture(userApi, userSet, null, null, null);
        CompletableFuture.allOf(agencyFuture, userFuture).join();
        List<FleetVo> fleetVoList = fleetDtoList.stream().map(fleetDto -> {
            FleetVo vo = new FleetVo();
            BeanUtils.copyProperties(fleetDto, vo);
            try {
                if (fleetDto.getAgencyId() != null) {
                    vo.setAgency((AgencySimpleVo) agencyFuture.get().get(fleetDto.getAgencyId()));
                }
                if (fleetDto.getManager() != null) {
                    vo.setManager((SysUserVo) userFuture.get().get(fleetDto.getManager()));
                }
            } catch (Exception e) {
                // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                e.printStackTrace();
            }
            vo.setTruckCount(truckFeign.count(fleetDto.getId()));
            vo.setDriverCount(driverFeign.count(fleetDto.getId()));
            return vo;
        }).collect(Collectors.toList());
        return PageResponse.<FleetVo>builder().items(fleetVoList).page(page).pagesize(pageSize).counts(fleetDtoPage.getCounts()).pages(fleetDtoPage.getPages()).build();
    }

    @ApiOperation(value = "获取车队详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车队id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/fleet/{id}")
    public FleetVo findFleetById(@PathVariable(name = "id") String id) {
        FleetDto dto = fleetFeign.fineById(id);
        FleetVo vo = new FleetVo();
        BeanUtils.copyProperties(dto, vo);
        //负责人信息
        if (StringUtils.isNotEmpty(dto.getManager())) {
            R<User> userResult = userApi.get(Long.valueOf(dto.getManager()));
            if (userResult.getIsSuccess() && userResult.getData() != null) {
                vo.setManager(BeanUtil.parseUser2Vo(userResult.getData(), null, null));
            }
        }
        //机构信息
        if (StringUtils.isNotEmpty(dto.getAgencyId())) {
            R<Org> orgResult = orgApi.get(Long.valueOf(dto.getAgencyId()));
            if (orgResult.getIsSuccess() && orgResult.getData() != null) {
                vo.setAgency(BeanUtil.parseOrg2SimpleVo(orgResult.getData()));
            }
        }
        vo.setTruckCount(truckFeign.count(dto.getId()));
        vo.setDriverCount(driverFeign.count(dto.getId()));
        return vo;
    }

    @ApiOperation(value = "删除车队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车队id", required = true, example = "1", paramType = "{path}")
    })
    @DeleteMapping("/fleet/{id}")
    public Result deleteFleet(@PathVariable(name = "id") String id) {
        fleetFeign.disable(id);
        return Result.ok();
    }

    @ApiOperation(value = "添加车辆")
    @PostMapping("/truck")
    public TruckVo saveTruck(@RequestBody TruckVo vo) {
        TruckDto dto = new TruckDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getFleet() != null) {
            dto.setFleetId(vo.getFleet().getId());
        }
        if (vo.getTruckType() != null) {
            dto.setTruckTypeId(vo.getTruckType().getId());
        }
        if (vo.getTruckLicenseId() != null) {
            // TODO: 2020/1/9 转化为具体对象
        }
        TruckDto resultDto = truckFeign.saveTruck(dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "更新车辆信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/truck/{id}")
    public TruckVo updateTruck(@PathVariable(name = "id") String id, @RequestBody TruckVo vo) {
        vo.setId(id);
        TruckDto dto = new TruckDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getFleet() != null) {
            dto.setFleetId(vo.getFleet().getId());
        }
        if (vo.getTruckType() != null) {
            dto.setTruckTypeId(vo.getTruckType().getId());
        }
        if (vo.getTruckLicenseId() != null) {
            // TODO: 2020/1/9 转化为具体对象
        }
        TruckDto resultDto = truckFeign.update(id, dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取车辆分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, example = "10"),
            @ApiImplicitParam(name = "truckTypeId", value = "车辆类型id"),
            @ApiImplicitParam(name = "licensePlate", value = "车牌号码"),
            @ApiImplicitParam(name = "fleetId", value = "所属车队id")
    })
    @GetMapping("/truck/page")
    public PageResponse<TruckVo> findTruckByPage(@RequestParam(name = "page") Integer page,
                                                 @RequestParam(name = "pageSize") Integer pageSize,
                                                 @RequestParam(name = "truckTypeId", required = false) String truckTypeId,
                                                 @RequestParam(name = "licensePlate", required = false) String licensePlate,
                                                 @RequestParam(name = "fleetId", required = false) String fleetId) {
        PageResponse<TruckDto> truckDtoPage = truckFeign.findByPage(page, pageSize, truckTypeId, licensePlate, fleetId);
        //加工数据
        List<TruckDto> truckDtoList = truckDtoPage.getItems();
        Set<String> truckTypeSet = new HashSet<>();
        Set<String> fleetSet = new HashSet<>();
        truckDtoList.forEach(truckDto -> {
            if (truckDto.getFleetId() != null) {
                fleetSet.add(truckDto.getFleetId());
            }
            if (truckDto.getTruckTypeId() != null) {
                truckTypeSet.add(truckDto.getTruckTypeId());
            }
        });
        CompletableFuture<Map> fleetFuture = PdCompletableFuture.fleetMapFuture(fleetFeign, fleetSet, null);
        CompletableFuture<Map> truckTypeFuture = PdCompletableFuture.truckTypeMapFuture(truckTypeFeign, truckTypeSet);
        CompletableFuture.allOf(fleetFuture, truckTypeFuture).join();
        List<TruckVo> truckVoList = truckDtoList.stream().map(dto -> {
            TruckVo vo = new TruckVo();
            BeanUtils.copyProperties(dto, vo);
            try {
                if (dto.getTruckTypeId() != null) {
                    vo.setTruckType((TruckTypeVo) truckTypeFuture.get().get(dto.getTruckTypeId()));
                }
                if (dto.getFleetId() != null) {
                    Map tmpMap = fleetFuture.get();
                    vo.setFleet((FleetVo) fleetFuture.get().get(dto.getFleetId()));
                }
            } catch (Exception e) {
                // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                e.printStackTrace();
            }
            return vo;
        }).collect(Collectors.toList());
        return PageResponse.<TruckVo>builder().items(truckVoList).page(page).pagesize(pageSize).counts(truckDtoPage.getCounts()).pages(truckDtoPage.getPages()).build();
    }

    @ApiOperation(value = "获取车辆详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/truck/{id}")
    public TruckVo findTruckById(@PathVariable(name = "id") String id) {
        TruckDto dto = truckFeign.fineById(id);
        TruckVo vo = new TruckVo();
        BeanUtils.copyProperties(dto, vo);
        //加工数据
        List<CompletableFuture> futureList = new ArrayList<>();
        if (dto.getFleetId() != null) {
            futureList.add(PdCompletableFuture.fleetFuture(fleetFeign, dto.getFleetId()));
        }
        if (dto.getTruckTypeId() != null) {
            futureList.add(PdCompletableFuture.truckTypeFuture(truckTypeFeign, dto.getTruckTypeId()));
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
        futureList.forEach(future -> {
            try {
                if (future.get() instanceof TruckTypeVo) {
                    vo.setTruckType((TruckTypeVo) future.get());
                }
                if (future.get() instanceof FleetVo) {
                    vo.setFleet((FleetVo) future.get());
                    if (vo.getFleet() != null && vo.getFleet().getAgency() != null && StringUtils.isNotEmpty(vo.getFleet().getAgency().getId())) {
                        R<Org> result = orgApi.get(Long.valueOf(vo.getFleet().getAgency().getId()));
                        if (result.getIsSuccess() && result.getData() != null) {
                            vo.setAgency(BeanUtil.parseOrg2Vo(result.getData(), null, null));
                        }
                    }
                }
            } catch (Exception e) {
                // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                e.printStackTrace();
            }

        });
        // TODO: 2020/2/24 车辆信息中的几个状态
        vo.setWorkStatus("空闲");
        vo.setLoadStatus("未满载");
        vo.setExpireStatus("未过期");
        return vo;
    }

    @ApiOperation(value = "删除车辆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆id", required = true, example = "1", paramType = "{path}")
    })
    @DeleteMapping("/truck/{id}")
    public Result deleteTruck(@PathVariable(name = "id") String id) {
        truckFeign.disable(id);
        return Result.ok();
    }

    @ApiOperation(value = "保存车辆行驶证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆id", required = true, example = "1", paramType = "{path}")
    })
    @PostMapping("/truck/{id}/license")
    public TruckLicenseVo saveTruckLicense(@PathVariable(name = "id") String id, @RequestBody TruckLicenseVo vo) {
        TruckLicenseDto dto = new TruckLicenseDto();
        BeanUtils.copyProperties(vo, dto);
        dto.setTruckId(id);
        TruckDto truckDto = truckFeign.fineById(id);
        dto.setId(truckDto.getTruckLicenseId());
        //加工数据
        if (StringUtils.isNotEmpty(vo.getExpirationDate())) {
            dto.setExpirationDate(LocalDate.parse(vo.getExpirationDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (StringUtils.isNotEmpty(vo.getMandatoryScrap())) {
            dto.setMandatoryScrap(LocalDate.parse(vo.getMandatoryScrap(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (StringUtils.isNotEmpty(vo.getRegistrationDate())) {
            dto.setRegistrationDate(LocalDate.parse(vo.getRegistrationDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (StringUtils.isNotEmpty(vo.getValidityPeriod())) {
            dto.setValidityPeriod(LocalDate.parse(vo.getValidityPeriod(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        TruckLicenseDto resultDto = truckLicenseFeign.saveTruckLicense(dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取车辆行驶证详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/truck/{id}/license")
    public TruckLicenseVo findTruckLicenseById(@PathVariable(name = "id") String id) {
        TruckDto dto = truckFeign.fineById(id);
        TruckLicenseDto truckLicenseDto = null;
        if (StringUtils.isNotEmpty(dto.getTruckLicenseId())) {
            truckLicenseDto = truckLicenseFeign.fineById(dto.getTruckLicenseId());
        }
        TruckLicenseVo vo = new TruckLicenseVo();
        if (truckLicenseDto != null) {
            BeanUtils.copyProperties(truckLicenseDto, vo);
            if (truckLicenseDto.getRegistrationDate() != null) {
                vo.setRegistrationDate(truckLicenseDto.getRegistrationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
            if (truckLicenseDto.getExpirationDate() != null) {
                vo.setExpirationDate(truckLicenseDto.getExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
            if (truckLicenseDto.getMandatoryScrap() != null) {
                vo.setMandatoryScrap(truckLicenseDto.getMandatoryScrap().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
            if (truckLicenseDto.getValidityPeriod() != null) {
                vo.setValidityPeriod(truckLicenseDto.getValidityPeriod().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }
        return vo;
    }

    @ApiOperation(value = "获取车辆车次信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车辆id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/truck/{id}/transportTrips")
    public List<TruckDriverVo> findTruckTransportTrips(@PathVariable(name = "id") String id) {
        List<TruckDriverVo> voList = new ArrayList<>();
        transportTripsFeign.findAllTruckDriverTransportTrips(null, id, null).forEach(transportTripsTruckDriverDto -> {
            TransportTripsDto transportTripsDto = transportTripsFeign.fineById(transportTripsTruckDriverDto.getTransportTripsId());
            if (transportTripsDto != null) {
                TransportLineDto transportLineDto = transportLineFeign.fineById(transportTripsDto.getTransportLineId());
                if (transportLineDto != null) {
                    TruckDriverVo vo = new TruckDriverVo();
                    TransportTripsVo transportTripsVo = new TransportTripsVo();
                    BeanUtils.copyProperties(transportTripsDto, transportTripsVo);
                    TransportLineVo transportLineVo = new TransportLineVo();
                    BeanUtils.copyProperties(transportLineDto, transportLineVo);
                    vo.setTransportTrips(transportTripsVo);
                    vo.setTransportLine(transportLineVo);
                    voList.add(vo);
                }
            }
        });
        return voList;
    }

    @ApiOperation(value = "添加线路")
    @PostMapping("/transportLine")
    public TransportLineVo saveTransportLine(@RequestBody TransportLineVo vo) {
        TransportLineDto dto = new TransportLineDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getAgency() != null) {
            dto.setAgencyId(vo.getAgency().getId());
        }
        if (vo.getStartAgency() != null) {
            dto.setStartAgencyId(vo.getStartAgency().getId());
        }
        if (vo.getEndAgency() != null) {
            dto.setEndAgencyId(vo.getEndAgency().getId());
        }
        if (vo.getTransportLineType() != null) {
            dto.setTransportLineTypeId(vo.getTransportLineType().getId());
        }
        // TODO: 2020/2/18 从token中获取所属机构id
        dto.setAgencyId("1");
        TransportLineDto resultDto = transportLineFeign.saveTransportLine(dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "更新线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "线路id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/transportLine/{id}")
    public TransportLineVo updateTransportLine(@PathVariable(name = "id") String id, @RequestBody TransportLineVo vo) {
        vo.setId(id);
        TransportLineDto dto = new TransportLineDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getAgency() != null) {
            dto.setAgencyId(vo.getAgency().getId());
        }
        if (vo.getStartAgency() != null) {
            dto.setStartAgencyId(vo.getStartAgency().getId());
        }
        if (vo.getEndAgency() != null) {
            dto.setEndAgencyId(vo.getEndAgency().getId());
        }
        if (vo.getTransportLineType() != null) {
            dto.setTransportLineTypeId(vo.getTransportLineType().getId());
        }
        TransportLineDto resultDto = transportLineFeign.update(id, dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取线路分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, example = "10"),
            @ApiImplicitParam(name = "name", value = "线路名称"),
            @ApiImplicitParam(name = "lineNumber", value = "线路编号"),
            @ApiImplicitParam(name = "transportLineTypeId", value = "线路类型id")
    })
    @GetMapping("/transportLine/page")
    public PageResponse<TransportLineVo> findTransportLineByPage(@RequestParam(name = "page") Integer page,
                                                                 @RequestParam(name = "pageSize") Integer pageSize,
                                                                 @RequestParam(name = "name", required = false) String name,
                                                                 @RequestParam(name = "transportLineTypeId", required = false) String transportLineTypeId,
                                                                 @RequestParam(name = "lineNumber", required = false) String lineNumber) {
        PageResponse<TransportLineDto> transportLineDtoPage = transportLineFeign.findByPage(page, pageSize, lineNumber, name, transportLineTypeId);
        //加工数据
        List<TransportLineDto> transportLineDtoList = transportLineDtoPage.getItems();
        Set<Long> agencySet = new HashSet<>();
        Set<String> transportLineTypeSet = new HashSet<>();
        transportLineDtoList.forEach(dto -> {
            if (dto.getAgencyId() != null) {
                agencySet.add(Long.valueOf(dto.getAgencyId()));
            }
            if (dto.getStartAgencyId() != null) {
                agencySet.add(Long.valueOf(dto.getStartAgencyId()));
            }
            if (dto.getEndAgencyId() != null) {
                agencySet.add(Long.valueOf(dto.getEndAgencyId()));
            }
            if (dto.getTransportLineTypeId() != null) {
                transportLineTypeSet.add(dto.getTransportLineTypeId());
            }
        });
        CompletableFuture<Map> agencyFuture = PdCompletableFuture.agencyMapFuture(orgApi, agencySet);
        CompletableFuture<Map> transportLineTypeFuture = PdCompletableFuture.transportLineTypeMapFuture(transportLineTypeFeign, transportLineTypeSet);
        CompletableFuture.allOf(agencyFuture, transportLineTypeFuture).join();
        List<TransportLineVo> transportLineVoList = transportLineDtoList.stream().map(dto -> {
            TransportLineVo vo = new TransportLineVo();
            BeanUtils.copyProperties(dto, vo);
            try {
                if (dto.getAgencyId() != null) {
                    vo.setAgency((AgencyVo) agencyFuture.get().get(dto.getAgencyId()));
                }
                if (dto.getStartAgencyId() != null) {
                    vo.setStartAgency((AgencyVo) agencyFuture.get().get(dto.getStartAgencyId()));
                }
                if (dto.getEndAgencyId() != null) {
                    vo.setEndAgency((AgencyVo) agencyFuture.get().get(dto.getEndAgencyId()));
                }
                if (dto.getTransportLineTypeId() != null) {
                    vo.setTransportLineType((TransportLineTypeVo) transportLineTypeFuture.get().get(dto.getTransportLineTypeId()));
                }
            } catch (Exception e) {
                // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                e.printStackTrace();
            }
            return vo;
        }).collect(Collectors.toList());
        return PageResponse.<TransportLineVo>builder().items(transportLineVoList).page(page).pagesize(pageSize).pages(transportLineDtoPage.getPages()).counts(transportLineDtoPage.getCounts()).build();
    }

    @ApiOperation(value = "获取线路详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "线路id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/transportLine/{id}")
    public TransportLineVo findTransportLineById(@PathVariable(name = "id") String id) {
        TransportLineDto dto = transportLineFeign.fineById(id);
        TransportLineVo vo = new TransportLineVo();
        BeanUtils.copyProperties(dto, vo);
        //加工数据
        Set<Long> agencySet = new HashSet<>();
        List<CompletableFuture> futureList = new ArrayList<>();
        if (dto.getAgencyId() != null) {
            agencySet.add(Long.valueOf(dto.getAgencyId()));
        }
        if (dto.getStartAgencyId() != null) {
            agencySet.add(Long.valueOf(dto.getStartAgencyId()));
        }
        if (dto.getEndAgencyId() != null) {
            agencySet.add(Long.valueOf(dto.getEndAgencyId()));
        }
        if (agencySet != null && agencySet.size() > 0) {
            futureList.add(PdCompletableFuture.agencyMapFuture(orgApi, agencySet));
        }
        if (dto.getTransportLineTypeId() != null) {
            futureList.add(PdCompletableFuture.transportLineTypeFuture(transportLineTypeFeign, dto.getTransportLineTypeId()));
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
        try {
            for (CompletableFuture future : futureList) {
                if (future.get() instanceof Map) {
                    Map<Long, AgencyVo> agencyMap = (Map<Long, AgencyVo>) future.get();
                    if (dto.getAgencyId() != null) {
                        vo.setAgency(agencyMap.get(dto.getAgencyId()));
                    }
                    if (dto.getStartAgencyId() != null) {
                        vo.setStartAgency(agencyMap.get(dto.getStartAgencyId()));
                    }
                    if (dto.getEndAgencyId() != null) {
                        vo.setEndAgency(agencyMap.get(dto.getEndAgencyId()));
                    }
                } else if (future.get() instanceof TransportLineTypeVo) {
                    vo.setTransportLineType((TransportLineTypeVo) future.get());
                }
            }
        } catch (Exception e) {
            // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
            e.printStackTrace();
        }
        return vo;
    }

    @ApiOperation(value = "删除线路")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "线路id", required = true, example = "1", paramType = "{path}")
    })
    @DeleteMapping("/transportLine/{id}")
    public Result deleteTransportLine(@PathVariable(name = "id") String id) {
        // TODO: 2020/1/10 注意检查线路，及其关联数据，确定是否可以删除
        transportLineFeign.disable(id);
        return Result.ok();
    }

    @ApiOperation(value = "添加车次")
    @PostMapping("/transportLine/trips")
    public TransportTripsVo saveTransportTrips(@RequestBody TransportTripsVo vo) {
        TransportTripsDto dto = new TransportTripsDto();
        BeanUtils.copyProperties(vo, dto);
        //加工数据
        if (vo.getTransportLine() != null) {
            dto.setTransportLineId(vo.getTransportLine().getId());
        }
        TransportTripsDto resultDto = transportTripsFeign.save(dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "更新车次")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车次id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/transportLine/trips/{id}")
    public TransportTripsVo updateTransportTrips(@PathVariable(name = "id") String id, @RequestBody TransportTripsVo vo) {
        vo.setId(id);
        TransportTripsDto dto = new TransportTripsDto();
        BeanUtils.copyProperties(vo, dto);
        //加工数据
        if (vo.getTransportLine() != null) {
            dto.setTransportLineId(vo.getTransportLine().getId());
        }
        TransportTripsDto resultDto = transportTripsFeign.update(id, dto);
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取车次列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "transportLineId", value = "线路id")
    })
    @GetMapping("/transportLine/trips")
    public List<TransportTripsVo> findAllTransportLineTrips(@RequestParam(name = "transportLineId", required = false) String transportLineId) {
        List<TransportTripsDto> transportTripsDtoList = transportTripsFeign.findAll(transportLineId, null);
        //加工数据
        Set<String> userSet = new HashSet<>();
        Set<String> truckSet = new HashSet<>();
        Set<String> transportLineSet = new HashSet<>();
        //建立车次与车辆和司机索引
        Map<String, List<TransportTripsTruckDriverDto>> transportTripsTruckDriverDtoMap = new HashMap<>();
        transportTripsDtoList.forEach(dto -> {
            if (dto.getTransportLineId() != null) {
                transportLineSet.add(dto.getTransportLineId());
                //建立车次司机索引
                List<String> drivers = new ArrayList<>();
                //建立车次车辆索引
                List<String> trucks = new ArrayList<>();
                List<TransportTripsTruckDriverDto> transportTripsTruckDriverDtoList = transportTripsFeign.findAllTruckDriverTransportTrips(dto.getId(), null, null);
                if (transportTripsTruckDriverDtoList != null) {
                    transportTripsTruckDriverDtoMap.put(dto.getId(), transportTripsTruckDriverDtoList);
                    transportTripsTruckDriverDtoList.forEach(transportTripsTruckDriverDto -> {
                        if (StringUtils.isNotEmpty(transportTripsTruckDriverDto.getTruckId())) {
                            trucks.add(transportTripsTruckDriverDto.getTruckId());
                        }
                        if (StringUtils.isNotEmpty(transportTripsTruckDriverDto.getUserId())) {
                            drivers.add(transportTripsTruckDriverDto.getUserId());
                        }
                    });
                }
                userSet.addAll(drivers);
                truckSet.addAll(trucks);
            }
        });
        CompletableFuture<Map> transportLineFuture = PdCompletableFuture.transportLineMapFuture(transportLineFeign, transportLineSet, null, null);
        CompletableFuture<Map> userFuture = PdCompletableFuture.userMapFuture(userApi, userSet, null, null, null);
        CompletableFuture<Map> truckFuture = PdCompletableFuture.truckMapFuture(truckFeign, truckSet, null);
        CompletableFuture.allOf(transportLineFuture, userFuture, truckFuture).join();
        return transportTripsDtoList.stream().map(dto -> {
            TransportTripsVo vo = new TransportTripsVo();
            BeanUtils.copyProperties(dto, vo);
            try {
                if (dto.getTransportLineId() != null) {
                    vo.setTransportLine((TransportLineVo) transportLineFuture.get().get(dto.getTransportLineId()));
                }
                if (transportTripsTruckDriverDtoMap.containsKey(dto.getId())) {
                    List<TransportTripsTruckDriverDto> transportTripsTruckDriverDtoList = transportTripsTruckDriverDtoMap.get(dto.getId());
                    List<TruckDriverVo> truckDriverVoList = new ArrayList<>();
                    for (TransportTripsTruckDriverDto transportTripsTruckDriverDto : transportTripsTruckDriverDtoList) {
                        TruckDriverVo truckDriverVo = new TruckDriverVo();
                        truckDriverVo.setTruck((TruckVo) truckFuture.get().get(transportTripsTruckDriverDto.getTruckId()));
                        if (StringUtils.isNotEmpty(transportTripsTruckDriverDto.getUserId())) {
                            TruckDriverDto driverDto = driverFeign.findOneDriver(transportTripsTruckDriverDto.getUserId());
                            if (driverDto != null) {
                                truckDriverVo.setDriver(BeanUtil.parseTruckDriverDto2Vo(driverDto, userApi, fleetFeign, orgApi));
                            }
                        }
                        if (truckDriverVo.getTruck() != null || truckDriverVo.getDriver() != null) {
                            truckDriverVoList.add(truckDriverVo);
                        }
                    }
                    vo.setTruckDrivers(truckDriverVoList);
                }
            } catch (Exception e) {
                // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
                e.printStackTrace();
            }
            vo.setPeriodName(Constant.TransportTripsPeriod.getEnumByPeriod(dto.getPeriod()) == null ? null : Constant.TransportTripsPeriod.getEnumByPeriod(dto.getPeriod()).getName());
            return vo;
        }).collect(Collectors.toList());
    }

    @ApiOperation(value = "获取车次详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车次id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/transportLine/trips/{id}")
    public TransportTripsVo findTransportLineTripsById(@PathVariable(name = "id") String id) {
        TransportTripsDto dto = transportTripsFeign.fineById(id);
        TransportTripsVo vo = new TransportTripsVo();
        BeanUtils.copyProperties(dto, vo);
        //加工数据
        List<CompletableFuture> futureList = new ArrayList<>();
        CompletableFuture<TransportLineVo> transportLineFuture = null;
        if (dto.getTransportLineId() != null) {
            transportLineFuture = PdCompletableFuture.transportLineFuture(transportLineFeign, dto.getTransportLineId());
            futureList.add(transportLineFuture);
        }
        Set<String> userSet = new HashSet<>();
        Set<String> truckSet = new HashSet<>();
        List<TransportTripsTruckDriverDto> transportTripsTruckDriverDtoList = transportTripsFeign.findAllTruckDriverTransportTrips(id, null, null);
        if (transportTripsTruckDriverDtoList != null) {
            transportTripsTruckDriverDtoList.forEach(transportTripsTruckDriverDto -> {
                if (StringUtils.isNotEmpty(transportTripsTruckDriverDto.getTruckId())) {
                    truckSet.add(transportTripsTruckDriverDto.getTruckId());
                }
                if (StringUtils.isNotEmpty(transportTripsTruckDriverDto.getUserId())) {
                    userSet.add(transportTripsTruckDriverDto.getUserId());
                }
            });
        }
        CompletableFuture<Map> userFuture = PdCompletableFuture.userMapFuture(userApi, userSet, null, null, null);
        futureList.add(userFuture);
        CompletableFuture<Map> truckFuture = PdCompletableFuture.truckMapFuture(truckFeign, truckSet, null);
        futureList.add(truckFuture);

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
        List<TruckDriverVo> truckDriverVoList = new ArrayList<>();

        try {
            if (transportLineFuture != null) {
                vo.setTransportLine(transportLineFuture.get());
            }
            if (transportTripsTruckDriverDtoList != null) {
                for (TransportTripsTruckDriverDto transportTripsTruckDriverDto : transportTripsTruckDriverDtoList) {
                    TruckDriverVo truckDriverVo = new TruckDriverVo();
                    truckDriverVo.setTruck((TruckVo) truckFuture.get().get(transportTripsTruckDriverDto.getTruckId()));
                    if (StringUtils.isNotEmpty(transportTripsTruckDriverDto.getUserId())) {
                        TruckDriverDto driverDto = driverFeign.findOneDriver(transportTripsTruckDriverDto.getUserId());
                        if (driverDto != null) {
                            truckDriverVo.setDriver(BeanUtil.parseTruckDriverDto2Vo(driverDto, userApi, fleetFeign, orgApi));
                        }
                    }
                    if (truckDriverVo.getTruck() != null || truckDriverVo.getDriver() != null) {
                        truckDriverVoList.add(truckDriverVo);
                    }
                }
            }
            vo.setTruckDrivers(truckDriverVoList);
        } catch (Exception e) {
            // TODO: 2020/1/2 此处异常处理依赖于业务是否为弱关系，如强关系，则返回错误
            e.printStackTrace();
        }
        vo.setPeriodName(Constant.TransportTripsPeriod.getEnumByPeriod(dto.getPeriod()) == null ? null : Constant.TransportTripsPeriod.getEnumByPeriod(dto.getPeriod()).getName());
        return vo;
    }

    @ApiOperation(value = "删除车次")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车次id", required = true, example = "1", paramType = "{path}")
    })
    @DeleteMapping("/transportLine/trips/{id}")
    public Result deleteTransportLineTrips(@PathVariable(name = "id") String id) {
        // TODO: 2020/1/10 需检查车次与其他关联数据的联系，决定是否可以删除
        transportTripsFeign.disable(id);
        return Result.ok();
    }

    @ApiOperation(value = "车次-安排车辆和司机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "车次id", required = true, example = "1", paramType = "{path}")
    })
    @PostMapping("/transportLine/trips/{id}/truckDriver")
    public Result saveTransportTripsTruck(@PathVariable(name = "id") String id, @RequestBody List<TruckDriverVo> truckDriverVoList) {
        //保存车辆安排信息
        transportTripsFeign.batchSaveTruckDriver(id, truckDriverVoList.stream().map(truckDriverVo -> {
            TransportTripsTruckDriverDto dto = new TransportTripsTruckDriverDto();
            if (truckDriverVo.getTruck() != null) {
                dto.setTruckId(truckDriverVo.getTruck().getId());
            }
            if (truckDriverVo.getDriver() != null) {
                dto.setUserId(truckDriverVo.getDriver().getUserId());
            }
            return dto;
        }).collect(Collectors.toList()));
        return Result.ok();
    }

    @ApiOperation(value = "获取司机分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, example = "10"),
            @ApiImplicitParam(name = "name", value = "司机名称"),
            @ApiImplicitParam(name = "username", value = "司机账号"),
            @ApiImplicitParam(name = "fleetId", value = "车队id")
    })
    @GetMapping("/driver/page")
    public PageResponse<DriverVo> findDriverByPage(@RequestParam(name = "page") Integer page,
                                                   @RequestParam(name = "pageSize") Integer pageSize,
                                                   @RequestParam(name = "name", required = false) String name,
                                                   @RequestParam(name = "username", required = false) String username,
                                                   @RequestParam(name = "fleetId", required = false) String fleetId) {
        List<DriverVo> driverVoList = new ArrayList<>();
        Long total = 0L;
        Long pages = 0L;
        //判断是否存在车队id
        if (StringUtils.isNotEmpty(fleetId)) {
            //当车队id存在时，以tms truckDriver为主
            PageResponse<TruckDriverDto> truckDriverDtoPage = driverFeign.findByPage(page, pageSize, fleetId);
            total = truckDriverDtoPage.getCounts();
            pages = truckDriverDtoPage.getPages();
            truckDriverDtoPage.getItems().forEach(driverDto -> {
                R<User> result = userApi.get(Long.valueOf(driverDto.getUserId()));
                if (result.getIsSuccess() && result.getData() != null) {
                    DriverVo driverVo = new DriverVo();
                    BeanUtils.copyProperties(BeanUtil.parseUser2Vo(result.getData(), null, orgApi), driverVo);
                    if (driverDto.getFleetId() != null) {
                        FleetDto fleetDto = fleetFeign.fineById(driverDto.getFleetId());
                        FleetVo fleetVo = new FleetVo();
                        BeanUtils.copyProperties(fleetDto, fleetVo);
                        driverVo.setFleet(fleetVo);
                    }
                    driverVoList.add(driverVo);
                }
            });
        } else {
            //否则以权限系统用户表为主
            R<Page<User>> result = userApi.page(page.longValue(), pageSize.longValue(), null, StaticStation.DRIVER_ID, name, username, null);
            if (result.getIsSuccess() && result.getData() != null) {
                total = result.getData().getTotal();
                pages = result.getData().getPages();
                List<String> userIds = result.getData().getRecords().stream().map(user -> String.valueOf(user.getId())).collect(Collectors.toList());
                Map<String, TruckDriverDto> driverDtoMap = driverFeign.findAllDriver(userIds, null).stream().collect(Collectors.toMap(TruckDriverDto::getUserId, dto -> dto));
                result.getData().getRecords().forEach(user -> {
                    DriverVo driverVo = new DriverVo();
                    BeanUtils.copyProperties(BeanUtil.parseUser2Vo(user, null, orgApi), driverVo);
                    TruckDriverDto driverDto = driverDtoMap.get(String.valueOf(user.getId()));
                    if (driverDto != null) {
                        BeanUtils.copyProperties(driverDto, driverVo);
                        //处理所属车队
                        if (driverDto.getFleetId() != null) {
                            FleetDto fleetDto = fleetFeign.fineById(driverDto.getFleetId());
                            FleetVo fleetVo = new FleetVo();
                            BeanUtils.copyProperties(fleetDto, fleetVo);
                            driverVo.setFleet(fleetVo);
                        }
                    }
                    driverVoList.add(driverVo);
                });
            }
        }
        //实现获取车辆线路等信息
        driverVoList.forEach(driverVo -> {
            List<TruckDriverVo> voList = new ArrayList<>();
            List<TransportTripsTruckDriverDto> transportTripsTruckDriverDtoList = transportTripsFeign.findAllTruckDriverTransportTrips(null, null, driverVo.getUserId());
            transportTripsTruckDriverDtoList.forEach(transportTripsTruckDriverDto -> {
                TransportTripsDto transportTripsDto = transportTripsFeign.fineById(transportTripsTruckDriverDto.getTransportTripsId());
                if (transportTripsDto != null&&StringUtils.isNotEmpty(transportTripsDto.getTransportLineId())) {
                    TransportLineDto transportLineDto = transportLineFeign.fineById(transportTripsDto.getTransportLineId());
                    if (transportLineDto != null) {
                        TruckDriverVo vo = new TruckDriverVo();
                        TransportTripsVo transportTripsVo = new TransportTripsVo();
                        BeanUtils.copyProperties(transportTripsDto, transportTripsVo);
                        TransportLineVo transportLineVo = new TransportLineVo();
                        BeanUtils.copyProperties(transportLineDto, transportLineVo);
                        vo.setTransportTrips(transportTripsVo);
                        vo.setTransportLine(transportLineVo);
                        TruckVo truckVo = new TruckVo();
                        TruckDto truckDto = truckFeign.fineById(transportTripsTruckDriverDto.getTruckId());
                        if (truckDto != null) {
                            BeanUtils.copyProperties(truckDto, truckVo);
                            vo.setTruck(truckVo);
                        }
                        voList.add(vo);
                    }
                }
            });
            if (voList.size() > 0) {
                driverVo.setTruck(voList.get(0).getTruck());
                driverVo.setTransportLine(voList.get(0).getTransportLine());
                driverVo.setTruckTransportTrip(voList.get(0).getTransportTrips());
                driverVo.setTruckTransportLine(voList.get(0).getTransportLine());
            }
        });
        return PageResponse.<DriverVo>builder().items(driverVoList).page(page).pagesize(pageSize).counts(total).pages(pages).build();
    }

    @ApiOperation(value = "获取司机基本信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "司机id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/driver/{id}")
    public DriverVo findDriverById(@PathVariable(name = "id") String id) {
        DriverVo vo = new DriverVo();
        R<User> userResult = userApi.get(Long.valueOf(id));
        if (userResult.getIsSuccess() && userResult.getData() != null) {
            BeanUtils.copyProperties(BeanUtil.parseUser2Vo(userResult.getData(), null, null), vo);
            if (userResult.getData().getOrgId() != null) {
                R<Org> result = orgApi.get(userResult.getData().getOrgId());
                if (result.getIsSuccess() && result.getData() != null) {
                    vo.setAgency(BeanUtil.parseOrg2SimpleVo(result.getData()));
                }
            }

            TruckDriverDto driverDto = driverFeign.findOneDriver(id);
            if (driverDto != null) {
                BeanUtils.copyProperties(driverDto, vo);
                if (driverDto.getFleetId() != null) {
                    FleetDto fleetDto = fleetFeign.fineById(driverDto.getFleetId());
                    FleetVo fleetVo = new FleetVo();
                    BeanUtils.copyProperties(fleetDto, fleetVo);
                    vo.setFleet(fleetVo);
                }
            }
        }
        return vo;
    }

    @ApiOperation(value = "保存司机信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "司机id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/driver/{id}")
    public DriverVo saveDriver(@PathVariable(name = "id") String id, @RequestBody DriverVo vo) {
        TruckDriverDto driverDto = driverFeign.findOneDriver(id);
        if (driverDto == null) {
            driverDto = new TruckDriverDto();
        }
        driverDto.setUserId(id);
        if (vo.getFleet() != null) {
            driverDto.setFleetId(vo.getFleet().getId());
        }
        driverDto.setAge(vo.getAge());
        driverFeign.saveDriver(driverDto);
        return vo;
    }

    @ApiOperation(value = "获取司机车辆安排")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "司机id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/driver/{id}/truck")
    public List<TruckDriverVo> findDriverTruckById(@PathVariable(name = "id") String id) {
        List<TruckDriverVo> voList = new ArrayList<>();
        transportTripsFeign.findAllTruckDriverTransportTrips(null, null, id).forEach(transportTripsTruckDriverDto -> {
            TransportTripsDto transportTripsDto = transportTripsFeign.fineById(transportTripsTruckDriverDto.getTransportTripsId());
            if (transportTripsDto != null) {
                TransportLineDto transportLineDto = transportLineFeign.fineById(transportTripsDto.getTransportLineId());
                if (transportLineDto != null) {
                    TruckDriverVo vo = new TruckDriverVo();
                    TransportTripsVo transportTripsVo = new TransportTripsVo();
                    BeanUtils.copyProperties(transportTripsDto, transportTripsVo);
                    TransportLineVo transportLineVo = new TransportLineVo();
                    BeanUtils.copyProperties(transportLineDto, transportLineVo);
                    vo.setTransportTrips(transportTripsVo);
                    vo.setTransportLine(transportLineVo);
                    TruckVo truckVo = new TruckVo();
                    TruckDto truckDto = truckFeign.fineById(transportTripsTruckDriverDto.getTruckId());
                    if (truckDto != null) {
                        BeanUtils.copyProperties(truckDto, truckVo);
                        vo.setTruck(truckVo);
                    }
                    voList.add(vo);
                }
            }
        });
        return voList;
    }

    @ApiOperation(value = "保存司机驾驶证信息")
    @PostMapping("/driverLicense")
    public DriverLicenseVo saveDriverLicense(@RequestBody DriverLicenseVo vo) {
        TruckDriverLicenseDto dto = new TruckDriverLicenseDto();
        BeanUtils.copyProperties(vo, dto);
        if (StringUtils.isNotEmpty(vo.getInitialCertificateDate())) {
            dto.setInitialCertificateDate(LocalDate.parse(vo.getInitialCertificateDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
        driverFeign.saveDriverLicense(dto);
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    @ApiOperation(value = "获取司机驾驶证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "司机id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/driverLicense/{id}")
    public DriverLicenseVo findDriverLicenseById(@PathVariable(name = "id") String id) {
        TruckDriverLicenseDto dto = driverFeign.findOneDriverLicense(id);
        DriverLicenseVo vo = new DriverLicenseVo();
        if (dto != null) {
            BeanUtils.copyProperties(dto, vo);
            if (dto.getInitialCertificateDate() != null) {
                vo.setInitialCertificateDate(dto.getInitialCertificateDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        }
        if (StringUtils.isEmpty(vo.getUserId())) {
            vo.setUserId(id);
        }
        return vo;
    }


}
