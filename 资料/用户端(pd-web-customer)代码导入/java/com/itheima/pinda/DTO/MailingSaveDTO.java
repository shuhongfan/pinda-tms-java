package com.itheima.pinda.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MailingSaveDTO {
    @ApiModelProperty("发件方地址簿id")
    private String sendAddress;
    @ApiModelProperty("收件方地址簿id")
    private String receiptAddress;
    @ApiModelProperty("取件时间")
    private String pickUpTime;
    @ApiModelProperty("取件方式")
    private Integer pickupType;
    @ApiModelProperty("付款方式,1.预结2到付")
    private Integer payMethod;
    @ApiModelProperty("物品类型")
    private String goodsType;
    @ApiModelProperty("物品名称")
    private String goodsName;
    @ApiModelProperty("物品重量")
    private String goodsWeight;
}
