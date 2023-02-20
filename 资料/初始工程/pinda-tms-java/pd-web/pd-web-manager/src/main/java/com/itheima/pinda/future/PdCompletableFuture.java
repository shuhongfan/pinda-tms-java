package com.itheima.pinda.future;

import com.itheima.pinda.DTO.angency.FleetDto;
import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.DTO.transportline.TransportLineDto;
import com.itheima.pinda.DTO.transportline.TransportLineTypeDto;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.DTO.truck.TruckTypeDto;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.enumeration.common.StaticStation;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.feign.agency.FleetFeign;
import com.itheima.pinda.feign.transportline.TransportLineFeign;
import com.itheima.pinda.feign.transportline.TransportLineTypeFeign;
import com.itheima.pinda.feign.truck.TruckFeign;
import com.itheima.pinda.feign.common.GoodsTypeFeign;
import com.itheima.pinda.feign.truck.TruckTypeFeign;
import com.itheima.pinda.util.BeanUtil;
import com.itheima.pinda.vo.base.angency.AgencySimpleVo;
import com.itheima.pinda.vo.base.angency.AgencyVo;
import com.itheima.pinda.vo.base.businessHall.GoodsTypeVo;
import com.itheima.pinda.vo.base.transforCenter.business.*;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PdCompletableFuture {
    /**
     * 获取map类型用户数据集合
     *
     * @param api     数据接口
     * @param userSet 用户id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> userMapFuture(UserApi api, Set<String> userSet, Integer station, String name, String agencyId) {
        return CompletableFuture.supplyAsync(() -> {
            //查询创建者信息列表
            Long stationId = null;
            if (station != null && station == Constant.UserStation.COURIER.getStation()) {
                stationId = StaticStation.COURIER_ID;
            } else if (station != null && station == Constant.UserStation.DRIVER.getStation()) {
                stationId = StaticStation.DRIVER_ID;
            }
            List<User> userList = new ArrayList<>();
            R<List<User>> result = api.list(userSet.stream().mapToLong(id -> Long.valueOf(id)).boxed().collect(Collectors.toList()), stationId, name, StringUtils.isNotEmpty(agencyId) ? Long.valueOf(agencyId) : null);
            if (result.getIsSuccess() && result.getData() != null) {
                userList.addAll(result.getData());
            }
            return userList.stream().map(user -> BeanUtil.parseUser2Vo(user, null, null)).collect(Collectors.toMap(SysUserVo::getUserId, vo -> vo));
        });
    }

    /**
     * 获取用户信息
     *
     * @param api 数据接口
     * @param id  用户id
     * @return 执行结果
     */
    public static final CompletableFuture<SysUserVo> userFuture(UserApi api, String id) {
        return CompletableFuture.supplyAsync(() -> {
            SysUserVo userVo = new SysUserVo();
            R<User> result = api.get(Long.valueOf(id));
            if (result.getIsSuccess() && result.getData() != null) {
                userVo = BeanUtil.parseUser2Vo(result.getData(), null, null);
            }
            return userVo;
        });
    }

    /**
     * 获取map类型机构数据集合
     *
     * @param api       数据接口
     * @param agencySet 机构id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> agencyMapFuture(OrgApi api, Set<Long> agencySet) {
        return CompletableFuture.supplyAsync(() -> {
            //查询所属机构信息列表
            R<List<Org>> result = api.list(null, new ArrayList<>(agencySet), null, null, null);
            if (result.getIsSuccess() && result.getData() != null) {
                List<Org> orgList = result.getData();
                Map<String, AgencyVo> voMap = new HashMap<>();
                orgList.forEach(org -> {
                    AgencyVo agencyVo = new AgencyVo();
                    agencyVo.setId(org.getId()+"");
                    BeanUtils.copyProperties(org, agencyVo);
                    if (!voMap.containsKey(agencyVo.getId())) {
                        voMap.put(agencyVo.getId(), agencyVo);
                    }
                });
                return voMap;
            }
            return null;
        });
    }

    /**
     * 获取map类型线路类型数据集合
     *
     * @param feign                数据接口
     * @param transportLineTypeSet 线路类型id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> transportLineTypeMapFuture(TransportLineTypeFeign feign, Set<String> transportLineTypeSet) {
        return CompletableFuture.supplyAsync(() -> {
            List<TransportLineTypeDto> transportLineTypeDtoList = feign.findAll(new ArrayList<>(transportLineTypeSet));
            return transportLineTypeDtoList.stream().map(dto -> {
                TransportLineTypeVo vo = new TransportLineTypeVo();
                BeanUtils.copyProperties(dto, vo);
                return vo;
            }).collect(Collectors.toMap(TransportLineTypeVo::getId, vo -> vo));
        });
    }

    /**
     * 获取map类型线路数据集合
     *
     * @param feign            数据接口
     * @param transportLineSet 线路id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> transportLineMapFuture(TransportLineFeign feign, Set<String> transportLineSet, String agencyId, List<String> agencyIds) {
        return CompletableFuture.supplyAsync(() -> {
            List<TransportLineDto> transportLineDtoList = feign.findAll(new ArrayList<>(transportLineSet), agencyId, agencyIds);
            return transportLineDtoList.stream().map(dto -> {
                TransportLineVo vo = new TransportLineVo();
                BeanUtils.copyProperties(dto, vo);
                return vo;
            }).collect(Collectors.toMap(TransportLineVo::getId, vo -> vo));
        });
    }

    /**
     * 获取map类型车队数据集合
     *
     * @param feign    数据接口
     * @param fleetSet 车队id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> fleetMapFuture(FleetFeign feign, Set<String> fleetSet, String agencyId) {
        return CompletableFuture.supplyAsync(() -> {
            //查询所属机构信息列表
            List<FleetDto> fleetList = feign.findAll(new ArrayList<>(fleetSet), agencyId);
            return fleetList.stream().map(fleetDto -> {
                FleetVo vo = new FleetVo();
                BeanUtils.copyProperties(fleetDto, vo);
                return vo;
            }).collect(Collectors.toMap(FleetVo::getId, vo -> vo));
        });
    }

    /**
     * 获取map类型车辆类型数据集合
     *
     * @param feign        数据接口
     * @param truckTypeSet 车辆类型id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> truckTypeMapFuture(TruckTypeFeign feign, Set<String> truckTypeSet) {
        return CompletableFuture.supplyAsync(() -> {
            List<TruckTypeDto> truckTypeDtoList = feign.findAll(new ArrayList<>(truckTypeSet));
            return truckTypeDtoList.stream().map(truckTypeDto -> {
                TruckTypeVo vo = new TruckTypeVo();
                BeanUtils.copyProperties(truckTypeDto, vo);
                return vo;
            }).collect(Collectors.toMap(TruckTypeVo::getId, vo -> vo));
        });
    }

    /**
     * 获取map类型车辆数据集合
     *
     * @param feign    数据接口
     * @param truckSet 车辆id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> truckMapFuture(TruckFeign feign, Set<String> truckSet, String fleetId) {
        return CompletableFuture.supplyAsync(() -> {
            List<TruckDto> truckDtoList = feign.findAll(new ArrayList<>(truckSet), fleetId);
            return truckDtoList.stream().map(dto -> {
                TruckVo vo = new TruckVo();
                BeanUtils.copyProperties(dto, vo);
                return vo;
            }).collect(Collectors.toMap(TruckVo::getId, vo -> vo));
        });
    }

    /**
     * 获取map类型货物类型数据集合
     *
     * @param feign        数据接口
     * @param goodsTypeSet 货物类型id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map> goodsTypeMapFuture(GoodsTypeFeign feign, Set<String> goodsTypeSet) {
        return CompletableFuture.supplyAsync(() -> {
            List<GoodsTypeDto> goodsTypeDtoList = feign.findAll(new ArrayList<>(goodsTypeSet));
            return goodsTypeDtoList.stream().map(goodsTypeDto -> {
                GoodsTypeVo vo = new GoodsTypeVo();
                BeanUtils.copyProperties(goodsTypeDto, vo);
                return vo;
            }).collect(Collectors.toMap(GoodsTypeVo::getId, vo -> vo));
        });
    }

    /**
     * 获取车辆类型信息
     *
     * @param feign 数据接口
     * @param id    车辆类型id
     * @return 执行结果
     */
    public static final CompletableFuture<TruckTypeVo> truckTypeFuture(TruckTypeFeign feign, String id) {
        return CompletableFuture.supplyAsync(() -> {
            //查询角色信息列表
            TruckTypeDto dto = feign.fineById(id);
            TruckTypeVo vo = new TruckTypeVo();
            BeanUtils.copyProperties(dto, vo);
            return vo;
        });
    }

    /**
     * 获取车队信息
     *
     * @param feign 数据接口
     * @param id    车队id
     * @return 执行结果
     */
    public static final CompletableFuture<FleetVo> fleetFuture(FleetFeign feign, String id) {
        return CompletableFuture.supplyAsync(() -> {
            //查询角色信息列表
            FleetDto dto = feign.fineById(id);
            FleetVo vo = new FleetVo();
            BeanUtils.copyProperties(dto, vo);
            if (StringUtils.isNotEmpty(dto.getAgencyId())) {
                AgencySimpleVo agencyVo = new AgencySimpleVo();
                agencyVo.setId(dto.getAgencyId());
                vo.setAgency(agencyVo);
            }
            return vo;
        });
    }

    /**
     * 获取机构数据列表
     *
     * @param api        数据接口
     * @param agencyType 机构类型
     * @param ids        机构id列表
     * @return 执行结果
     */
    public static final CompletableFuture<List<Org>> agencyListFuture(OrgApi api, Integer agencyType, List<Long> ids, Long countyId) {
        return CompletableFuture.supplyAsync(() -> {
            R<List<Org>> result = api.list(agencyType, ids, countyId, null, null);
            if (result.getIsSuccess()) {
                return result.getData();
            }
            return new ArrayList<>();
        });
    }

    /**
     * 获取车辆类型列表
     *
     * @param feign 数据接口
     * @param ids   车辆类型id列表
     * @return 执行结果
     */
    public static final CompletableFuture<List<TruckTypeDto>> truckTypeListFuture(TruckTypeFeign feign, List<String> ids) {
        return CompletableFuture.supplyAsync(() -> feign.findAll(ids));
    }

    /**
     * 获取货物类型列表
     *
     * @param feign 数据接口
     * @param ids   货物类型id列表
     * @return 执行结果
     */
    public static final CompletableFuture<List<GoodsTypeDto>> goodsTypeListFuture(GoodsTypeFeign feign, List<String> ids) {
        return CompletableFuture.supplyAsync(() -> feign.findAll(ids));
    }

    /**
     * 获取机构详情
     *
     * @param api 数据接口
     * @param id  机构id
     * @return 执行结果
     */
//    public static final CompletableFuture<AgencyVo> agencyFuture(OrgApi api, Long id) {
//        return CompletableFuture.supplyAsync(() -> {
//            R<Org> result = api.get(id);
//            if (result.getIsSuccess()&&result.getData()!=null) {
//                AgencySimpleVo agencyVo = ;
//                //                // TODO: 2020/3/17 数据处理
//                return BeanUtil.parseOrg2SimpleVo(result.getData());
//            }
//            return null;
//        });
//    }

    /**
     * 获取线路类型信息
     *
     * @param feign 数据接口
     * @param id    线路类型id
     * @return 执行结果
     */
    public static final CompletableFuture<TransportLineTypeVo> transportLineTypeFuture(TransportLineTypeFeign feign, String id) {
        return CompletableFuture.supplyAsync(() -> {
            TransportLineTypeDto dto = feign.fineById(id);
            TransportLineTypeVo vo = new TransportLineTypeVo();
            BeanUtils.copyProperties(dto, vo);
            return vo;
        });
    }

    /**
     * 获取线路信息
     *
     * @param feign 数据接口
     * @param id    线路id
     * @return 执行结果
     */
    public static final CompletableFuture<TransportLineVo> transportLineFuture(TransportLineFeign feign, String id) {
        return CompletableFuture.supplyAsync(() -> {
            TransportLineDto dto = feign.fineById(id);
            TransportLineVo vo = new TransportLineVo();
            BeanUtils.copyProperties(dto, vo);
            return vo;
        });
    }
}
