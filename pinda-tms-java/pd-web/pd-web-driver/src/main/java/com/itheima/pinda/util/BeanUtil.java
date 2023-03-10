package com.itheima.pinda.util;

import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.RoleApi;
import com.itheima.pinda.authority.dto.auth.RoleDTO;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.enumeration.common.StaticStation;
import com.itheima.pinda.authority.enumeration.core.OrgType;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.vo.*;
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
}
