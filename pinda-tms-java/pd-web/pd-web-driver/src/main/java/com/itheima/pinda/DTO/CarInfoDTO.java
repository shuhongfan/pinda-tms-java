package com.itheima.pinda.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CarInfoDTO implements Serializable {
    /**
     * 车辆id
     */
    @ApiModelProperty("车辆id")
    private String id;

    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    private String brand;

    /**
     * 车型
     */
    @ApiModelProperty("车型")
    private String carModel;

    /**
     * 尺寸
     */
    @ApiModelProperty("尺寸")
    private String carSize;

    /**
     * 车辆牌照
     */
    @ApiModelProperty("车辆牌照")
    private String licensePlate;

    /**
     * 载重
     */
    @ApiModelProperty("载重")
    private String load;

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String picture;

}
