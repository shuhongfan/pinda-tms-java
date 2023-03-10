package com.itheima.pinda.util;

import com.itheima.pinda.DTO.*;
import com.itheima.pinda.DTO.angency.FleetDto;
import com.itheima.pinda.DTO.transportline.TransportTripsDto;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.DTO.user.TruckDriverDto;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.RoleApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.dto.auth.RoleDTO;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.enumeration.common.StaticStation;
import com.itheima.pinda.authority.enumeration.core.OrgType;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.feign.OrderFeign;
import com.itheima.pinda.feign.TransportOrderFeign;
import com.itheima.pinda.feign.TransportTaskFeign;
import com.itheima.pinda.feign.agency.FleetFeign;
import com.itheima.pinda.feign.transportline.TransportTripsFeign;
import com.itheima.pinda.feign.truck.TruckFeign;
import com.itheima.pinda.vo.base.angency.AgencySimpleVo;
import com.itheima.pinda.vo.base.angency.AgencyVo;
import com.itheima.pinda.vo.base.angency.RoleVo;
import com.itheima.pinda.vo.base.AreaSimpleVo;
import com.itheima.pinda.vo.base.transforCenter.business.DriverVo;
import com.itheima.pinda.vo.base.transforCenter.business.FleetVo;
import com.itheima.pinda.vo.oms.OrderVo;
import com.itheima.pinda.vo.base.transforCenter.business.TransportTripsVo;
import com.itheima.pinda.vo.base.transforCenter.business.TruckVo;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import com.itheima.pinda.vo.work.DriverJobVo;
import com.itheima.pinda.vo.work.TaskPickupDispatchVo;
import com.itheima.pinda.vo.work.TaskTransportVo;
import com.itheima.pinda.vo.work.TransportOrderVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BeanUtil {
    public static SysUserVo parseUser2Vo(User user, RoleApi roleApi, OrgApi orgApi) {
        SysUserVo vo = new SysUserVo();
        //填充基本信息
        vo.setUserId(String.valueOf(user.getId()));
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setMobile(user.getMobile());
        vo.setUsername(user.getAccount());
        vo.setName(user.getName());
        // TODO: 2020/3/19 员工编号待实现
        //处理角色信息
        if (roleApi != null) {
            R<List<RoleDTO>> result = roleApi.list(user.getId());
            List<RoleVo> roles = new ArrayList<>();
            if (result.getIsSuccess() && result.getData() != null) {
                result.getData().forEach(role -> {
                    roles.add(parseRole2Vo(role));
                });
            }
            vo.setRoles(roles);
        }
        //处理所属机构信息
        if (orgApi != null && user.getOrgId() != null && user.getOrgId() != 0) {
            R<Org> result = orgApi.get(user.getOrgId());
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setAgency(parseOrg2SimpleVo(result.getData()));
            }
        }
        //处理岗位信息
        if (user.getStationId() != null && user.getStationId() != 0) {
            if (user.getStationId() == StaticStation.COURIER_ID) {
                vo.setStation(Constant.UserStation.COURIER.getStation());
                vo.setStationName(Constant.UserStation.COURIER.getName());
            } else if (user.getStationId() == StaticStation.DRIVER_ID) {
                vo.setStation(Constant.UserStation.DRIVER.getStation());
                vo.setStationName(Constant.UserStation.DRIVER.getName());
            } else {
                vo.setStation(Constant.UserStation.PERSONNEL.getStation());
                vo.setStationName(Constant.UserStation.PERSONNEL.getName());
            }
        }
        return vo;
    }

    /**
     * 角色数据模型转换
     *
     * @param role
     * @return
     */
    public static RoleVo parseRole2Vo(RoleDTO role) {
        RoleVo vo = new RoleVo();
        vo.setId(String.valueOf(role.getId()));
        vo.setName(role.getName());
        return vo;
    }


    /**
     * 机构数据模型转换
     *
     * @param org
     * @return
     */
    public static AgencyVo parseOrg2Vo(Org org, OrgApi orgApi, AreaApi areaApi) {
        AgencyVo agencyVo = new AgencyVo();
        agencyVo.setId(org.getId() + "");
        agencyVo.setName(org.getName());
        if (org.getOrgType() != null) {
            agencyVo.setAgencyType(org.getOrgType());
            agencyVo.setAgencyTypeName(OrgType.getEnumByType(org.getOrgType()).getName());
        }
        agencyVo.setAddress(org.getAddress());
        agencyVo.setLongitude(org.getLongitude());
        agencyVo.setLatitude(org.getLatitude());
        agencyVo.setContractNumber(org.getContractNumber());
        agencyVo.setStatus(org.getStatus() ? 0 : 1);
        // TODO: 2020/3/17 处理负责人信息
        //处理父级信息
        if (org.getParentId() != null && org.getParentId() != 0 && orgApi != null) {
            R<Org> result = orgApi.get(org.getParentId());
            if (result.getIsSuccess() && result.getData() != null && result.getData().getId() != null) {
                AgencySimpleVo simpleVo = new AgencySimpleVo();
                BeanUtils.copyProperties(result.getData(), simpleVo);
                simpleVo.setId(String.valueOf(result.getData().getId()));
                agencyVo.setParent(simpleVo);
            }
        }
        //处理省市区信息
        Set<Long> areaIds = new HashSet<>();
        boolean provinceOk = org.getProvinceId() != null && org.getProvinceId() != 0;
        boolean cityOk = org.getCityId() != null && org.getCityId() != 0;
        boolean countyOk = org.getCountyId() != null && org.getCountyId() != 0;
        if (provinceOk) {
            areaIds.add(org.getProvinceId());
        }
        if (cityOk) {
            areaIds.add(org.getCityId());
        }
        if (countyOk) {
            areaIds.add(org.getCountyId());
        }
        if (areaIds.size() > 0 && areaApi != null) {
            R<List<Area>> result = areaApi.findAll(null, new ArrayList<>(areaIds));
            if (result.getIsSuccess()) {
                Map<Long, Area> areaMap = result.getData().stream().collect(Collectors.toMap(Area::getId, area -> area));
                if (provinceOk) {
                    agencyVo.setProvince(parseArea2Vo(areaMap.get(org.getProvinceId())));
                }
                if (cityOk) {
                    agencyVo.setCity(parseArea2Vo(areaMap.get(org.getCityId())));
                }
                if (countyOk) {
                    agencyVo.setCounty(parseArea2Vo(areaMap.get(org.getCountyId())));
                }
            }
        }
        return agencyVo;
    }

    public static AgencySimpleVo parseOrg2SimpleVo(Org org) {
        AgencySimpleVo vo = new AgencySimpleVo();
        vo.setId(String.valueOf(org.getId()));
        vo.setName(org.getName());
        return vo;
    }

    public static AreaSimpleVo parseArea2Vo(Area area) {
        AreaSimpleVo vo = new AreaSimpleVo();
        if (area != null && area.getId() != null) {
            BeanUtils.copyProperties(area, vo);
            vo.setId(String.valueOf(area.getId()));
        }
        return vo;
    }

    public static OrderDTO parseOrderVo2DTO(OrderVo vo) {
        OrderDTO dto = new OrderDTO();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getSenderProvince() != null) {
            dto.setSenderProvinceId(vo.getSenderProvince().getId());
        }
        if (vo.getSenderCity() != null) {
            dto.setSenderCityId(vo.getSenderCity().getId());
        }
        if (vo.getSenderCounty() != null) {
            dto.setSenderCountyId(vo.getSenderCounty().getId());
        }
        if (vo.getReceiverProvince() != null) {
            dto.setReceiverProvinceId(vo.getReceiverProvince().getId());
        }
        if (vo.getReceiverCity() != null) {
            dto.setReceiverCityId(vo.getReceiverCity().getId());
        }
        if (vo.getReceiverCounty() != null) {
            dto.setReceiverCountyId(vo.getReceiverCounty().getId());
        }
        return dto;
    }

    public static OrderVo parseOrderDTO2Vo(OrderDTO dto, AreaApi areaApi) {
        OrderVo vo = new OrderVo();
        BeanUtils.copyProperties(dto, vo);
        if (dto.getSenderProvinceId() != null && areaApi != null) {
            R<Area> result = areaApi.get(Long.valueOf(dto.getSenderProvinceId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setSenderProvince(parseArea2Vo(result.getData()));
            }
        }
        if (dto.getSenderCityId() != null && areaApi != null) {
            R<Area> result = areaApi.get(Long.valueOf(dto.getSenderCityId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setSenderCity(parseArea2Vo(result.getData()));
            }
        }
        if (dto.getSenderCountyId() != null && areaApi != null) {
            R<Area> result = areaApi.get(Long.valueOf(dto.getSenderCountyId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setSenderCounty(parseArea2Vo(result.getData()));
            }
        }
        if (dto.getReceiverProvinceId() != null && areaApi != null) {
            R<Area> result = areaApi.get(Long.valueOf(dto.getReceiverProvinceId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setReceiverProvince(parseArea2Vo(result.getData()));
            }
        }
        if (dto.getReceiverCityId() != null && areaApi != null) {
            R<Area> result = areaApi.get(Long.valueOf(dto.getReceiverCityId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setReceiverCity(parseArea2Vo(result.getData()));
            }
        }
        if (dto.getReceiverCountyId() != null && areaApi != null) {
            R<Area> result = areaApi.get(Long.valueOf(dto.getReceiverCountyId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setReceiverCounty(parseArea2Vo(result.getData()));
            }
        }
        return vo;
    }

    public static TaskPickupDispatchDTO parseTaskPickupDispatchVo2DTO(TaskPickupDispatchVo vo) {
        TaskPickupDispatchDTO dto = new TaskPickupDispatchDTO();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getAgency() != null) {
            dto.setAgencyId(vo.getAgency().getId());
        }
        if (vo.getCourier() != null) {
            dto.setCourierId(vo.getCourier().getUserId());
        }
        if (vo.getOrder() != null) {
            dto.setOrderId(vo.getOrder().getId());
            if (vo.getOrder().getSenderProvince() != null) {
                dto.setSenderProvinceId(vo.getOrder().getSenderProvince().getId());
            }
            if (vo.getOrder().getSenderCity() != null) {
                dto.setSenderCityId(vo.getOrder().getSenderCity().getId());
            }
            if (StringUtils.isNotEmpty(vo.getOrder().getSenderName())) {
                dto.setSenderName(vo.getOrder().getSenderName());
            }
            if (vo.getOrder().getReceiverProvince() != null) {
                dto.setReceiverProvinceId(vo.getOrder().getReceiverProvince().getId());
            }
            if (vo.getOrder().getReceiverCity() != null) {
                dto.setReceiverCityId(vo.getOrder().getReceiverCity().getId());
            }
            if (StringUtils.isNotEmpty(vo.getOrder().getReceiverName())) {
                dto.setReceiverName(vo.getOrder().getReceiverName());
            }
        }
        return dto;
    }

    public static TaskPickupDispatchVo parseTaskPickupDispatchDTO2Vo(TaskPickupDispatchDTO dto, OrderFeign orderFeign, AreaApi areaApi, OrgApi orgApi, UserApi userApi) {
        TaskPickupDispatchVo vo = new TaskPickupDispatchVo();
        BeanUtils.copyProperties(dto, vo);
        if (StringUtils.isNotEmpty(dto.getOrderId()) && orderFeign != null) {
            OrderDTO orderDTO = orderFeign.findById(dto.getOrderId());
            if (orderDTO != null) {
                vo.setOrder(parseOrderDTO2Vo(orderDTO, areaApi));
            }
        }
        if (StringUtils.isNotEmpty(dto.getAgencyId()) && orgApi != null) {
            R<Org> result = orgApi.get(Long.valueOf(dto.getAgencyId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setAgency(parseOrg2SimpleVo(result.getData()));
            }
        }
        if (StringUtils.isNotEmpty(dto.getCourierId()) && userApi != null) {
            R<User> result = userApi.get(Long.valueOf(dto.getCourierId()));
            if (result.getIsSuccess() && result.getData() != null) {
                vo.setCourier(parseUser2Vo(result.getData(), null, null));
            }
        }
        return vo;
    }

    public static TransportOrderDTO parseTransportOrderVo2DTO(TransportOrderVo vo) {
        TransportOrderDTO dto = new TransportOrderDTO();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getOrder() != null) {
            dto.setOrderId(vo.getOrder().getId());
        }
        return dto;
    }

    public static TransportOrderVo parseTransportOrderDTO2Vo(TransportOrderDTO dto, OrderFeign orderFeign, AreaApi areaApi) {
        TransportOrderVo vo = new TransportOrderVo();
        BeanUtils.copyProperties(dto, vo);
        if (StringUtils.isNotEmpty(dto.getOrderId()) && orderFeign != null) {
            OrderDTO orderDTO = orderFeign.findById(dto.getOrderId());
            if (orderDTO != null) {
                vo.setOrder(parseOrderDTO2Vo(orderDTO, areaApi));
            }
        }
        return vo;
    }

    public static TaskTransportDTO parseTaskTransportVo2DTO(TaskTransportVo vo) {
        TaskTransportDTO dto = new TaskTransportDTO();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getTransportTrips() != null) {
            dto.setTransportTripsId(vo.getTransportTrips().getId());
        }
        if (vo.getStartAgency() != null) {
            dto.setStartAgencyId(vo.getStartAgency().getId());
        }
        if (vo.getEndAgency() != null) {
            dto.setEndAgencyId(vo.getEndAgency().getId());
        }
        if (vo.getTruck() != null) {
            dto.setTruckId(vo.getTruck().getId());
        }
        if (vo.getTransportOrders() != null && vo.getTransportOrders().size() > 0) {
            dto.setTransportOrderIds(vo.getTransportOrders().stream().filter(transOrder -> StringUtils.isNotEmpty(transOrder.getId())).map(transOrder -> transOrder.getId()).collect(Collectors.toList()));
        }
        return dto;
    }

    public static TaskTransportVo parseTaskTransportDTO2Vo(TaskTransportDTO dto, TransportTripsFeign transportTripsFeign, OrgApi orgApi, UserApi userApi, TruckFeign truckFeign, TransportOrderFeign transportOrderFeign, OrderFeign orderFeign, AreaApi areaApi) {
        TaskTransportVo vo = new TaskTransportVo();
        BeanUtils.copyProperties(dto, vo);
        if (StringUtils.isNotEmpty(dto.getTransportTripsId()) && transportTripsFeign != null) {
            TransportTripsDto transportTripsDto = transportTripsFeign.fineById(dto.getTransportTripsId());
            if (transportTripsDto != null) {
                TransportTripsVo transportTripsVo = new TransportTripsVo();
                BeanUtils.copyProperties(transportTripsDto, transportTripsVo);
                vo.setTransportTrips(transportTripsVo);
            }
        }
        if (StringUtils.isNotEmpty(dto.getStartAgencyId()) && orgApi != null) {
            R<Org> orgResult = orgApi.get(Long.valueOf(dto.getStartAgencyId()));
            if (orgResult.getIsSuccess() && orgResult.getData() != null) {
                vo.setStartAgency(parseOrg2SimpleVo(orgResult.getData()));
            }
        }
        if (StringUtils.isNotEmpty(dto.getEndAgencyId()) && orgApi != null) {
            R<Org> orgResult = orgApi.get(Long.valueOf(dto.getEndAgencyId()));
            if (orgResult.getIsSuccess() && orgResult.getData() != null) {
                vo.setEndAgency(parseOrg2SimpleVo(orgResult.getData()));
            }
        }
        if (StringUtils.isNotEmpty(dto.getTruckId()) && truckFeign != null) {
            TruckDto truckDto = truckFeign.fineById(dto.getTruckId());
            if (truckDto != null) {
                TruckVo truckVo = new TruckVo();
                BeanUtils.copyProperties(truckDto, truckVo);
                vo.setTruck(truckVo);
            }
        }
        List<TransportOrderVo> transportOrderVoList = new ArrayList<>();
        if (dto.getTransportOrderIds() != null && dto.getTransportOrderIds().size() > 0 && transportOrderFeign != null) {
            dto.getTransportOrderIds().forEach(transportOrderId -> {
                if (StringUtils.isNotEmpty(transportOrderId)) {
                    TransportOrderDTO transportOrderDTO = transportOrderFeign.findById(transportOrderId);
                    if (transportOrderDTO != null) {
                        transportOrderVoList.add(parseTransportOrderDTO2Vo(transportOrderDTO, orderFeign, areaApi));
                    }
                }
            });
        }
        vo.setTransportOrders(transportOrderVoList);
        // TODO: 2020/4/9 司机信息待实现
        return vo;
    }

    public static DriverJobDTO parseDriverJobVo2DTO(DriverJobVo vo) {
        DriverJobDTO dto = new DriverJobDTO();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getStartAgency() != null) {
            dto.setStartAgencyId(vo.getStartAgency().getId());
        }
        if (vo.getEndAgency() != null) {
            dto.setEndAgencyId(vo.getEndAgency().getId());
        }
        if (vo.getDriver() != null) {
            dto.setDriverId(vo.getDriver().getUserId());
        }
        if (vo.getTaskTransport() != null) {
            dto.setTaskTransportId(vo.getTaskTransport().getId());
        }
        return dto;
    }

    public static DriverJobVo parseDriverJobDTO2Vo(DriverJobDTO dto, TransportTripsFeign transportTripsFeign, OrgApi orgApi, UserApi userApi, TruckFeign truckFeign, TransportOrderFeign transportOrderFeign, OrderFeign orderFeign, AreaApi areaApi, TransportTaskFeign transportTaskFeign) {
        DriverJobVo vo = new DriverJobVo();
        BeanUtils.copyProperties(dto, vo);
        if (StringUtils.isNotEmpty(dto.getStartAgencyId()) && orgApi != null) {
            R<Org> orgResult = orgApi.get(Long.valueOf(dto.getStartAgencyId()));
            if (orgResult.getIsSuccess() && orgResult.getData() != null) {
                vo.setStartAgency(parseOrg2SimpleVo(orgResult.getData()));
            }
        }
        if (StringUtils.isNotEmpty(dto.getEndAgencyId()) && orgApi != null) {
            R<Org> orgResult = orgApi.get(Long.valueOf(dto.getEndAgencyId()));
            if (orgResult.getIsSuccess() && orgResult.getData() != null) {
                vo.setEndAgency(parseOrg2SimpleVo(orgResult.getData()));
            }
        }
        if (StringUtils.isNotEmpty(dto.getDriverId()) && userApi != null) {
            R<User> userResult = userApi.get(Long.valueOf(dto.getDriverId()));
            if (userResult.getIsSuccess() && userResult.getData() != null) {
                vo.setDriver(parseUser2Vo(userResult.getData(), null, null));
            }
        }
        if (StringUtils.isNotEmpty(dto.getTaskTransportId()) && transportTaskFeign != null) {
            TaskTransportDTO taskTransportDTO = transportTaskFeign.findById(dto.getTaskTransportId());
            if (taskTransportDTO != null) {
                vo.setTaskTransport(parseTaskTransportDTO2Vo(taskTransportDTO, transportTripsFeign, orgApi, userApi, truckFeign, transportOrderFeign, orderFeign, areaApi));
            }
        }
        return vo;
    }

    public static DriverVo parseTruckDriverDto2Vo(TruckDriverDto dto, UserApi userApi, FleetFeign fleetFeign,OrgApi orgApi) {
        DriverVo vo = new DriverVo();
        if (StringUtils.isNotEmpty(dto.getUserId()) && userApi != null) {
            R<User> userResult = userApi.get(Long.valueOf(dto.getUserId()));
            if (userResult.getIsSuccess() && userResult.getData() != null) {
                vo.setUserId(dto.getUserId());
                BeanUtils.copyProperties(userResult.getData(), vo);
                if (userResult.getData().getOrgId()!=null){
                    R<Org> orgResult = orgApi.get(userResult.getData().getOrgId());
                    if (orgResult.getIsSuccess() && orgResult.getData() != null) {
                        vo.setAgency(parseOrg2SimpleVo(orgResult.getData()));
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(dto.getFleetId()) && fleetFeign != null) {
            FleetDto fleetDto = fleetFeign.fineById(dto.getFleetId());
            FleetVo fleetVo = new FleetVo();
            BeanUtils.copyProperties(fleetDto, fleetVo);
            vo.setFleet(fleetVo);
        }
        return vo;
    }
}
