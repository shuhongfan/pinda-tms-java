package com.itheima.pinda.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("位置信息")
public class LocationEntity {
    public String getId() {
        return businessId + "#" + type + "#" + currentTime;
    }

    /**
     * 车辆Id
     */
    @ApiModelProperty("业务id, 快递员id 或者  车辆id")
    private String businessId;

    /**
     * 司机名称
     */
    @ApiModelProperty("司机名称")
    private String name;

    /**
     * 司机电话
     */
    @ApiModelProperty("司机电话")
    private String phone;

    /**
     * 车牌号
     */
    @ApiModelProperty("licensePlate")
    private String licensePlate;

    /**
     * 类型
     */
    @ApiModelProperty("类型，车辆：truck,快递员：courier")
    private String type;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String lng;

    /**
     * 维度
     */
    @ApiModelProperty("维度")
    private String lat;

    /**
     * 当前时间
     */
    @ApiModelProperty("当前时间 格式：yyyyMMddHHmmss")
    private String currentTime;

    @ApiModelProperty("所属车队")
    private String team;

    @ApiModelProperty("运输任务id")
    private String transportTaskId;
}