package com.itheima.pinda.DTO.webManager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskPickupDispatchQueryDTO {
    @ApiModelProperty("当前页数")
    private Integer page = 1;
    @ApiModelProperty("每页条数")
    private Integer pageSize = 10;
    @ApiModelProperty("任务类型，1为取件任务，2为派件任务")
    private Integer taskType;
    @ApiModelProperty("任务状态，1为待执行（对应 待上门和须交接）、2为进行中（该状态暂不使用，属于保留状态）、3为待确认（对应 待妥投和须交件）、4为已完成、5为已取消")
    private Integer status;
    @ApiModelProperty("运单id")
    private String transportOrderId;
    @ApiModelProperty("快递员姓名")
    private String courierName;
    @ApiModelProperty("发件人省份id")
    private String senderProvinceId;
    @ApiModelProperty("发件人城市id")
    private String senderCityId;
    @ApiModelProperty("发件人姓名")
    private String senderName;
    @ApiModelProperty("收件人省份id")
    private String receiverProvinceId;
    @ApiModelProperty("收件人城市id")
    private String receiverCityId;
    @ApiModelProperty("收件人姓名")
    private String receiverName;
}
