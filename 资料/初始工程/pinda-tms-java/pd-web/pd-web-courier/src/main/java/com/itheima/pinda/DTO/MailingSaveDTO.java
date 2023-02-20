package com.itheima.pinda.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("预估总价模型")
public class MailingSaveDTO {
    /**
     * 付款方式,1.预结2到付
     */
    @ApiModelProperty("付款方式 1预结 2到付")
    private Integer payMethod;
    @ApiModelProperty("物品类型")
    private String goodsType;
    @ApiModelProperty("物品名称")
    private String goodsName;
    @ApiModelProperty("物品重量")
    private String goodsWeight;
    @ApiModelProperty("物品体积")
    private String goodsVolume;
    @ApiModelProperty("订单号")
    private String orderNumber;
}
