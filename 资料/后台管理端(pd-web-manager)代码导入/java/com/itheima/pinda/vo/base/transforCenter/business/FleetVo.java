package com.itheima.pinda.vo.base.transforCenter.business;

import com.itheima.pinda.vo.base.angency.AgencySimpleVo;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "车队信息")
public class FleetVo implements Serializable {
    private static final long serialVersionUID = -1658601432859261332L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "车队名称")
    private String name;
    @ApiModelProperty(value = "车队编号")
    private String fleetNumber;
    @ApiModelProperty(value = "所属机构")
    private AgencySimpleVo agency;
    @ApiModelProperty(value = "负责人")
    private SysUserVo manager;
    @ApiModelProperty(value = "车辆总数")
    private Integer truckCount;
    @ApiModelProperty(value = "司机总数")
    private Integer driverCount;
    @ApiModelProperty(value = "车队司机")
    private List<SysUserVo> drivers;
    @ApiModelProperty(value = "车队车辆")
    private List<TruckVo> trucks;
}
