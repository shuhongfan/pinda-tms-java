package com.itheima.pinda.vo.oms;

import com.itheima.pinda.vo.base.businessHall.GoodsTypeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 货品总重量
 * </p>
 *
 * @author jpf
 * @since 2019-12-26
 */
@Data
@ApiModel(value = "货物信息")
public class OrderCargoVo implements Serializable {
    private static final long serialVersionUID = -2953242040093337789L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "订单信息")
    private OrderVo order;

    @ApiModelProperty(value = "运单id")
    private String tranOrderId;

    @ApiModelProperty(value = "货物类型信息")
    private GoodsTypeVo goodsType;

    @ApiModelProperty(value = "货物名称")
    private String name;

    @ApiModelProperty(value = "货物单位")
    private String unit;

    @ApiModelProperty(value = "货品货值")
    private BigDecimal cargoValue;

    @ApiModelProperty(value = "货品条码")
    private String cargoBarcode;

    @ApiModelProperty(value = "货品数量")
    private Integer quantity;

    @ApiModelProperty(value = "货品体积")
    private BigDecimal volume;

    @ApiModelProperty(value = "货品重量")
    private BigDecimal weight;

    @ApiModelProperty(value = "货品备注")
    private String remark;

    @ApiModelProperty(value = "货品总体积")
    private BigDecimal totalVolume;

    @ApiModelProperty(value = "货品总重量")
    private BigDecimal totalWeight;
}
