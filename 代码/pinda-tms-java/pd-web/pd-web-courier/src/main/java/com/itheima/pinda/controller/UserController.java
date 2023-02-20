package com.itheima.pinda.controller;


import com.itheima.pinda.DTO.UserProfileDTO;
import com.itheima.pinda.DTO.user.CourierScopeDto;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.context.RequestContext;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.feign.user.CourierScopeFeign;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 运单表 前端控制器
 * </p>
 *
 * @author diesel
 * @since 2020-03-19
 */
@Slf4j
@Api(tags = "用户管理")
@Controller
@RequestMapping("user")
public class UserController {

    private final UserApi userApi;

    private final OrgApi orgApi;

    private final CourierScopeFeign courierScopeFeign;

    private final AreaApi areaApi;

    public UserController(UserApi userApi, OrgApi orgApi, CourierScopeFeign courierScopeFeign, AreaApi areaApi) {
        this.userApi = userApi;
        this.orgApi = orgApi;
        this.courierScopeFeign = courierScopeFeign;
        this.areaApi = areaApi;
    }

    @SneakyThrows
    @ApiOperation(value = "我的信息")
    @ResponseBody
    @GetMapping("profile")
    public Result profile() {

        //  快递员id  并放入参数
        String courierId = RequestContext.getUserId();
        // 基本信息
        R<User> userR = userApi.get(Long.valueOf(courierId));
        User user = userR.getData();
        // 所属机构
        R<Org> orgR = orgApi.get(user.getOrgId());
        Org org = orgR.getData();

        //
        List<CourierScopeDto> courierScopeDtos = courierScopeFeign.findAllCourierScope(null, user.getId().toString());
        List<Long> areaIds = courierScopeDtos.stream().map(item -> Long.valueOf(item.getAreaId())).collect(Collectors.toList());
        R<List<Area>> areaDtosR = areaApi.findAll(null, areaIds);
        List<Area> areas = areaDtosR.getData();
        return Result.ok().put("data", UserProfileDTO.builder()
                .id(user.getId().toString())
                .avatar(user.getAvatar())
                .name(user.getName())
                .phone(user.getMobile())
                .manager(org.getName())
                .areas(areas)
                .build());
    }
}
