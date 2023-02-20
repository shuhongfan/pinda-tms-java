package com.itheima.pinda.vo.base.transforCenter.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "车辆和司机关联信息")
public class TruckDriverVo implements Serializable {
    private static final long serialVersionUID = 6343522146170548498L;
    @ApiModelProperty(value = "司机")
    private DriverVo driver;
    @ApiModelProperty(value = "车辆")
    private TruckVo truck;
    @ApiModelProperty(value = "线路信息")
    private TransportLineVo transportLine;
    @ApiModelProperty(value = "车次信息")
    private TransportTripsVo transportTrips;
}