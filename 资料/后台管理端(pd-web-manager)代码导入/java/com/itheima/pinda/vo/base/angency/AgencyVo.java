package com.itheima.pinda.vo.base.angency;

import com.itheima.pinda.vo.base.AreaSimpleVo;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "机构信息")
public class AgencyVo implements Serializable {
    private static final long serialVersionUID = 3417821750914521294L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构类型 1为分公司，2为一级转运中心 3为二级转运中心 4为网点")
    private Integer agencyType;

    @ApiModelProperty(value = "机构类型名称")
    private String agencyTypeName;

    @ApiModelProperty(value = "所属省份")
    private AreaSimpleVo province;

    @ApiModelProperty(value = "所属城市")
    private AreaSimpleVo city;

    @ApiModelProperty(value = "所属区县")
    private AreaSimpleVo county;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "经度")
    private String longitude;

    @ApiModelProperty(value = "纬度")
    private String latitude;

    @ApiModelProperty(value = "父级机构")
    private AgencySimpleVo parent;

    @ApiModelProperty(value = "负责人")
    private SysUserVo manager;

    @ApiModelProperty(value = "联系电话")
    private String contractNumber;

    @ApiModelProperty(value = "状态 0：禁用 1：正常")
    private Integer status;
}
