package com.itheima.pinda.vo.base.businessHall;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.itheima.pinda.vo.base.transforCenter.business.TruckTypeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * GoodsTypeVo
 */
@Data
@ApiModel(value = "货物类型信息")
public class GoodsTypeVo implements Serializable {
    private static final long serialVersionUID = -6733081065775505503L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "货物类型名称")
    private String name;
    @ApiModelProperty(value = "默认重量，单位：千克")
    private BigDecimal defaultWeight;
    @ApiModelProperty(value = "默认体积，单位：方")
    private BigDecimal defaultVolume;
    @ApiModelProperty(value = "说明")
    private String remark;
    @ApiModelProperty(value = "车辆类型")
    private List<TruckTypeVo> truckTypes;
}