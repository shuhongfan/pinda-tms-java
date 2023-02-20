package com.itheima.pinda.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserProfileDTO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 用户头
     */
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String manager;

    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String name;

    /**
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;

    /**
     * 所属车队
     */
    @ApiModelProperty("所属车队")
    private String team;

    /**
     * 所属转运中心
     */
    @ApiModelProperty("所属转运中心")
    private String transport;

    /**
     * 司机编号
     */
    @ApiModelProperty("司机编号")
    private String userNumber;

    /**
     * 车辆id
     */
    @ApiModelProperty("车辆id")
    private String truckId;

    @ApiModelProperty("车牌号")
    private String licensePlate;

    @ApiModelProperty("运输任务id")
    private String transportTaskId;

}
