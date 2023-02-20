package com.itheima.pinda.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "用户信息")
public class SysUserVo implements Serializable {
    private static final long serialVersionUID = -3424962804442674755L;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "员工账号")
    private String username;
    @ApiModelProperty(value = "员工姓名")
    private String name;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "工号")
    private String workNumber;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "所属机构信息")
    private AgencySimpleVo agency;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "岗位 1为员工 2为快递员 3为司机")
    private Integer station;
    @ApiModelProperty(value = "岗位名称")
    private String stationName;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "账号状态 0：禁用   1：正常")
    private Integer status;
    @ApiModelProperty(value = "创建者信息")
    private SysUserVo creator;
    @ApiModelProperty(value = "创建时间,格式: yyyy-MM-dd HH:mm:ss")
    private String createTime;
    @ApiModelProperty(value = "角色信息")
    private List<RoleVo> roles;
}
