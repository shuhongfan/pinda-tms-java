package com.itheima.pinda.DTO.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * GoodsTypeDto
 */
@Data
public class GoodsTypeDto implements Serializable {
    private static final long serialVersionUID = -6890776123674482761L;
    /**
     * id
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 货物类型名称
     */
    @ApiModelProperty("物品类型名称")
    @NotNull
    private String name;
    /**
     * 默认重量，单位：千克
     */
    @ApiModelProperty("默认重量")
    private BigDecimal defaultWeight;
    /**
     * 默认体积，单位：方
     */
    @ApiModelProperty("默认体积")
    private BigDecimal defaultVolume;
    /**
     * 说明
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 车辆类型id列表
     */
    private List<String> truckTypeIds;
    /**
     * 状态 0：禁用 1：正常
     */
    @ApiModelProperty("状态 0：禁用 1：正常")
    @NotNull
    @Max(value = 1)
    @Min(value = 0)
    private Integer status;
}