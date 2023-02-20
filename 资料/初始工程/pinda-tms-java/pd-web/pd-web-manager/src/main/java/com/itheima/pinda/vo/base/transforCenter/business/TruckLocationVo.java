package com.itheima.pinda.vo.base.transforCenter.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "车辆位置信息")
public class TruckLocationVo implements Serializable {
    private static final long serialVersionUID = 8540398115062991784L;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "电话")
    private String mobile;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "车辆类型名称")
    private String truckTypeName;
    @ApiModelProperty(value = "车牌号")
    private String licensePlate;
}
