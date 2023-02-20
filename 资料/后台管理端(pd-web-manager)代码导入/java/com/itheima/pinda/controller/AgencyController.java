package com.itheima.pinda.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.RoleApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.dto.core.OrgTreeDTO;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.enumeration.core.OrgType;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.EntCoordSyncJob;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.angency.AgencyScopeDto;
import com.itheima.pinda.feign.agency.AgencyScopeFeign;
import com.itheima.pinda.util.BeanUtil;
import com.itheima.pinda.vo.base.AreaSimpleVo;
import com.itheima.pinda.vo.base.angency.*;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("agency")
@Api(tags = "组织管理")
@Log
public class AgencyController {
    @Autowired
    private OrgApi orgApi;
    @Autowired
    private AreaApi areaApi;
    @Autowired
    private UserApi userApi;
    @Autowired
    private RoleApi roleApi;
    @Autowired
    private AgencyScopeFeign agencyScopeFeign;

    @ApiOperation(value = "获取树状机构信息")
    @GetMapping("/tree")
    public List<AgencySimpleVo> treeAgency() {
        List<AgencySimpleVo> resultList = new ArrayList<>();

        R<List<OrgTreeDTO>> result = orgApi.tree(null, true);
        if (result.getIsSuccess() && result.getData() != null && result.getData().size() > 0) {
            resultList.addAll(result.getData().stream().map(orgTreeDTO -> {
                AgencySimpleVo simpleVo = BeanUtil.parseOrg2SimpleVo(orgTreeDTO);
                simpleVo.setSubAgencies(getNode(orgTreeDTO.getChildren()));
                return simpleVo;
            }).collect(Collectors.toList()));
        }
        return resultList;
    }

    @ApiOperation(value = "获取机构详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "机构id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/{id}")
    public AgencyVo findAgencyById(@PathVariable(name = "id") String id) {
        R<Org> result = orgApi.get(Long.valueOf(id));
        if (result.getIsSuccess()) {
            Org org = result.getData();
            if (org != null) {
                AgencyVo vo = BeanUtil.parseOrg2Vo(org, orgApi, areaApi);
                return vo;
            }
        }
        return null;
    }

    @ApiOperation(value = "获取员工详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "员工id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/user/{id}")
    public SysUserVo findById(@PathVariable(name = "id") String id) {
        R<User> result = userApi.get(Long.valueOf(id));
        SysUserVo vo = null;
        if (result.getIsSuccess() && result.getData() != null) {
            vo = BeanUtil.parseUser2Vo(result.getData(), roleApi, orgApi);
        }
        return vo;
    }

    @ApiOperation(value = "获取员工分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页尺寸", required = true, example = "10"),
            @ApiImplicitParam(name = "agencyId", value = "机构id")
    })
    @GetMapping("/user/page")
    public PageResponse<SysUserVo> findUserByPage(@RequestParam(name = "page") Integer page,
                                                  @RequestParam(name = "pageSize") Integer pageSize,
                                                  @RequestParam(name = "agencyId", required = false) String agencyId) {
        R<Page<User>> result = userApi.page(page.longValue(), pageSize.longValue(), StringUtils.isNotEmpty(agencyId) ? Long.valueOf(agencyId) : null, null, null, null, null);
        if (result.getIsSuccess() && result.getData() != null) {
            IPage<User> userPage = result.getData();
            //处理对象转换
            List<SysUserVo> voList = userPage.getRecords().stream().map(user -> BeanUtil.parseUser2Vo(user, roleApi, orgApi)).collect(Collectors.toList());
            return PageResponse.<SysUserVo>builder().items(voList).page(page).pagesize(pageSize).counts(userPage.getTotal()).pages(userPage.getPages()).build();
        }
        return PageResponse.<SysUserVo>builder().items(new ArrayList<>()).page(page).pagesize(pageSize).counts(0L).pages(0L).build();
    }

    @ApiOperation(value = "保存机构业务范围")
    @PostMapping("/scope")
    public Result saveScope(@RequestBody AgencyScopeVo vo) {
        //验证和处理范围和区域信息
        Result result = validateParam(vo);
        if (!"0".equals(result.get("code").toString())) {
            return result;
        }
        //保存前先清理一遍
        AgencyScopeDto deleteDto = new AgencyScopeDto();
        deleteDto.setAgencyId(vo.getAgency().getId());
        agencyScopeFeign.deleteAgencyScope(deleteDto);

        //保存数据
        List<AgencyScopeDto> saveList = vo.getAreas().stream().map(areaVo -> {
            AgencyScopeDto dto = new AgencyScopeDto();
            dto.setAreaId(areaVo.getId());
            dto.setAgencyId(vo.getAgency().getId());
            dto.setMutiPoints(areaVo.getMutiPoints());
            return dto;
        }).collect(Collectors.toList());
        agencyScopeFeign.batchSaveAgencyScope(saveList);
        return Result.ok();
    }

    /**
     * 验证范围参数设置区域id
     *
     * @param vo
     * @return
     */
    private Result validateParam(AgencyScopeVo vo) {
        List<AreaSimpleVo> areas = vo.getAreas();
        if (areas == null || areas.size() == 0) {
            return Result.error(5000, "范围信息为空");
        } else {
            for (AreaSimpleVo areaSimpleVo : areas) {
                String adcodeOld = "";
                Area area = new Area();
                //一个区域的多个范围
                List<List<Map>> list = areaSimpleVo.getMutiPoints();
                if (list == null || list.size() == 0) {
                    return Result.error(5000, "范围信息为空");
                } else {
                    for (List<Map> listMap : list) {
                        for(int i=0;i<listMap.size();i++){
                            Map pointMap = listMap.get(i);
                            String point = getPoint(pointMap);
                            Map map = EntCoordSyncJob.getLocationByPosition(point);
                            String adcode = map.getOrDefault("adcode", "").toString();
                            if (StringUtils.isBlank(adcode)) {
                                return Result.error(5000, "根据地图获取区划编码为空");
                            } else {
                                if (!StringUtils.equals(adcode, adcodeOld) && i>0) {
                                    return Result.error(5000, "一个机构作业范围必须在一个区域内");
                                }
                                R<Area> r = areaApi.getByCode(adcode + "000000");
                                if (r.getIsSuccess() && r.getData() != null) {
                                    area = r.getData();
                                }
                            }
                            adcodeOld = adcode;
                        }

                    }
                }
                areaSimpleVo.setId(area.getId() + "");
                areaSimpleVo.setName(area.getName());
            }

        }
        return Result.ok();
    }

    private String getPoint(Map pointMap) {
        String lng = pointMap.getOrDefault("lng","").toString();
        String lat = pointMap.getOrDefault("lat","").toString();
        return lng+","+lat;
    }

    @ApiOperation(value = "获取机构业务范围")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "机构id", required = true, example = "1", paramType = "{path}")
    })
    @GetMapping("/{id}/scope")
    public AgencyScopeVo findAllAgencyScope(@PathVariable(name = "id") String id) {
        R<Org> result = orgApi.get(Long.valueOf(id));
        if (result.getIsSuccess()) {
            Org org = result.getData();
            List<AgencyScopeDto> agencyScopeDtoList = null;
            if (org != null && org.getOrgType() != null) {
                if (org.getOrgType() == OrgType.BUSINESS_HALL.getType()) {
                    //当前机构为网点
                    agencyScopeDtoList = getAgencyScopes(id, null);
                } else if (org.getOrgType() == OrgType.SECONDARY_TRANSFER_CENTER.getType()) {
                    //当前机构为二级转运中心¬
                    List<String> agencyIds = getOrgIds(Long.valueOf(id), null).stream().map(item -> String.valueOf(item)).collect(Collectors.toList());
                    agencyScopeDtoList = getAgencyScopes(null, agencyIds);
                } else if (org.getOrgType() == OrgType.TOP_TRANSFER_CENTER.getType()) {
                    //当前机构为一级转运中心
                    List<Long> parentIds = getOrgIds(Long.valueOf(id), null);
                    if (parentIds.size() > 0) {
                        List<String> agencyIds = getOrgIds(null, parentIds).stream().map(item -> String.valueOf(item)).collect(Collectors.toList());
                        agencyScopeDtoList = getAgencyScopes(null, agencyIds);
                    }
                } else if (org.getOrgType() == OrgType.BRANCH_OFFICE.getType()) {
                    //当前机构为分公司
                    List<Long> firstLevels = getOrgIds(Long.valueOf(id), null);
                    if (firstLevels.size() > 0) {
                        List<Long> secondLevels = getOrgIds(null, firstLevels);
                        if (secondLevels.size() > 0) {
                            List<String> agencyIds = getOrgIds(null, secondLevels).stream().map(item -> String.valueOf(item)).collect(Collectors.toList());
                            agencyScopeDtoList = getAgencyScopes(null, agencyIds);
                        }
                    }
                }
            }
            //处理返回信息
            AgencyScopeVo vo = new AgencyScopeVo();
            AgencyVo agencyVo = org == null ? null : BeanUtil.parseOrg2Vo(org, orgApi, areaApi);
            vo.setAgency(agencyVo);
            List<AreaSimpleVo> areas = new ArrayList<>();
            if (agencyScopeDtoList != null) {
                List<Long> areaIds = agencyScopeDtoList.stream().map(dto -> Long.valueOf(dto.getAreaId())).collect(Collectors.toList());
                if (areaIds.size() > 0) {
                    R<List<Area>> areaResult = areaApi.findAll(null, areaIds);
                    if (areaResult.getIsSuccess() && areaResult.getData() != null) {
                        areas.addAll(areaResult.getData().stream().map(BeanUtil::parseArea2Vo).collect(Collectors.toList()));
                    }
                }
            }
            vo.setAreas(addMutiPoints(areas, agencyScopeDtoList));
            return vo;
        }
        return null;
    }

    /**
     * 返回结果中添加区域内的作业范围
     * @param areas
     * @param agencyScopeDtoList
     * @return
     */
    private List<AreaSimpleVo> addMutiPoints(List<AreaSimpleVo> areas, List<AgencyScopeDto> agencyScopeDtoList) {
        for (AreaSimpleVo areaSimpleVo : areas) {
            for (AgencyScopeDto agencyScopeDto : agencyScopeDtoList) {
                if (agencyScopeDto.getAreaId().equals(areaSimpleVo.getId())){
                    areaSimpleVo.setMutiPoints(agencyScopeDto.getMutiPoints());
                }
            }
        }
        return areas;
    }

    /**
     * 获取子级组织id列表
     *
     * @param id  组织id
     * @param ids 组织id列表
     * @return 子级组织id列表
     */
    private List<Long> getOrgIds(Long id, List<Long> ids) {
        R<List<Org>> listResult = orgApi.list(null, null, null, id, ids);
        if (listResult.getIsSuccess() && listResult.getData() != null && listResult.getData().size() > 0) {
            return listResult.getData().stream().map(Org::getId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<AgencyScopeDto> getAgencyScopes(String id, List<String> ids) {
        boolean idOk = StringUtils.isNotEmpty(id) && !id.equals("0");
        boolean idsOk = ids != null && ids.size() > 0;
        if (idOk || idsOk) {
            return agencyScopeFeign.findAllAgencyScope(null, id, ids, null);
        }
        return new ArrayList<>();
    }

    /**
     * 递归获取组织树
     *
     * @param dtoList
     * @return
     */
    private List<AgencySimpleVo> getNode(List<OrgTreeDTO> dtoList) {
        List<AgencySimpleVo> list = new ArrayList<>();
        if (dtoList != null && dtoList.size() > 0) {
            for (int i = 0; i < dtoList.size(); i++) {
                AgencySimpleVo vo = BeanUtil.parseOrg2SimpleVo(dtoList.get(i));
                vo.setSubAgencies(getNode(dtoList.get(i).getChildren()));
                list.add(vo);
            }
        }
        return list;
    }
}
