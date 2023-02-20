package com.itheima.pinda.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "角色信息")
public class RoleVo implements Serializable {
    private static final long serialVersionUID = 1597893585759987194L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "机构类型")
    private Integer agencyType;
    @ApiModelProperty(value = "机构类型名称")
    private String agencyTypeName;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "最近更新时间")
    private String lastUpdateTime;
    @ApiModelProperty(value = "最近更新人")
    private SysUserVo updater;
}
