package com.itheima.pinda.vo.base.transforCenter.business;

import com.itheima.pinda.vo.base.angency.AgencySimpleVo;
import com.itheima.pinda.vo.base.angency.AgencyVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "司机基本信息")
public class DriverVo implements Serializable {
    private static final long serialVersionUID = 6475689247966513088L;
    @ApiModelProperty(value = "司机id")
    private String userId;
    @ApiModelProperty(value = "司机姓名")
    private String name;
    @ApiModelProperty(value = "工号")
    private String workNumber;
    @ApiModelProperty(value = "所属机构信息")
    private AgencySimpleVo agency;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "所属车队")
    private FleetVo fleet;
    @ApiModelProperty(value = "使用车辆")
    private TruckVo truck;
    @ApiModelProperty(value = "车辆线路")
    private TransportLineVo truckTransportLine;
    @ApiModelProperty(value = "车辆所属车次")
    private TransportTripsVo truckTransportTrip;
    @ApiModelProperty(value = "线路")
    private TransportLineVo transportLine;
    @ApiModelProperty(value = "工作状态")
    private String workStatus;
    @ApiModelProperty(value = "年龄")
    private Integer age;
}
