package com.itheima.pinda.vo.base.transforCenter.business;

import com.itheima.pinda.vo.base.angency.AgencyVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "车辆信息")
public class TruckVo implements Serializable {
    private static final long serialVersionUID = 8540398115062991784L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "车辆类型")
    private TruckTypeVo truckType;
    @ApiModelProperty(value = "所属车队")
    private FleetVo fleet;
    @ApiModelProperty(value = "品牌")
    private String brand;
    @ApiModelProperty(value = "车牌号码")
    private String licensePlate;
    @ApiModelProperty(value = "GPS设备id")
    private String deviceGpsId;
    @ApiModelProperty(value = "准载重量")
    private BigDecimal allowableLoad;
    @ApiModelProperty(value = "准载体积")
    private BigDecimal allowableVolume;
    @ApiModelProperty(value = "车辆行驶证信息id")
    private String truckLicenseId;
    @ApiModelProperty(value = "所属机构信息")
    private AgencyVo agency;
    @ApiModelProperty(value = "工作状态")
    private String workStatus;
    @ApiModelProperty(value = "过期状态")
    private String expireStatus;
    @ApiModelProperty(value = "装载状态")
    private String loadStatus;
}
