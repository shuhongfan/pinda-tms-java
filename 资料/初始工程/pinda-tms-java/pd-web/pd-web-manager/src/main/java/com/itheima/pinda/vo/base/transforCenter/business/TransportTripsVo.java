package com.itheima.pinda.vo.base.transforCenter.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "车次信息")
public class TransportTripsVo implements Serializable {
    private static final long serialVersionUID = 3496683581406652811L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "车次名称")
    private String name;
    @ApiModelProperty(value = "发车时间")
    private String departureTime;
    @ApiModelProperty(value = "到达时间")
    private String arrivalTime;
    @ApiModelProperty(value = "所属线路")
    private TransportLineVo transportLine;
    @ApiModelProperty(value = "周期，1为天，2为周，3为月")
    private Integer period;
    @ApiModelProperty(value = "周期名称")
    private String periodName;
    @ApiModelProperty(value = "所选车辆和司机")
    private List<TruckDriverVo> truckDrivers;
}
