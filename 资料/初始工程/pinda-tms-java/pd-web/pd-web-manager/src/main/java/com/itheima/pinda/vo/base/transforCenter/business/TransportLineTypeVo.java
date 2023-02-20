package com.itheima.pinda.vo.base.transforCenter.business;

import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "线路类型")
public class TransportLineTypeVo implements Serializable {
    private static final long serialVersionUID = -6081155291135396513L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "线路类型名称")
    private String name;
    @ApiModelProperty(value = "线路类型编码")
    private String typeNumber;
    @ApiModelProperty(value = "起始地机构类型")
    private Integer startAgencyType;
    @ApiModelProperty(value = "起始地机构类型名称")
    private String startAgencyTypeName;
    @ApiModelProperty(value = "目的地机构类型")
    private Integer endAgencyType;
    @ApiModelProperty(value = "目的地机构类型名称")
    private String endAgencyTypeName;
    @ApiModelProperty(value = "最后更新时间")
    private String lastUpdateTime;
    @ApiModelProperty(value = "更新人")
    private SysUserVo updater;
    @ApiModelProperty(value = "状态 0：禁用 1：正常")
    private Integer status;
}
