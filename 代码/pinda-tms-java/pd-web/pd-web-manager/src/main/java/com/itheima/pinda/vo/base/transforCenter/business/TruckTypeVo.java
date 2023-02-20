package com.itheima.pinda.vo.base.transforCenter.business;

import com.itheima.pinda.vo.base.businessHall.GoodsTypeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "车辆类型")
public class TruckTypeVo implements Serializable {
    private static final long serialVersionUID = 3017388977673057982L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "车辆类型名称")
    private String name;
    @ApiModelProperty(value = "准载重量")
    private BigDecimal allowableLoad;
    @ApiModelProperty(value = "准载体积")
    private BigDecimal allowableVolume;
    @ApiModelProperty(value = "长")
    private BigDecimal measureLong;
    @ApiModelProperty(value = "宽")
    private BigDecimal measureWidth;
    @ApiModelProperty(value = "高")
    private BigDecimal measureHigh;
    @ApiModelProperty(value = "货物类型列表")
    private List<GoodsTypeVo> goodsTypes;
}
