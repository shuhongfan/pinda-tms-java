package com.itheima.pinda.DTO.webManager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TransportOrderQueryDTO {
    @ApiModelProperty("当前页数")
    private Integer page = 1;
    @ApiModelProperty("每页条数")
    private Integer pageSize = 10;
    @ApiModelProperty("运单id")
    private String id;
    @ApiModelProperty("运单状态(1.新建 2.已装车，发往x转运中心 3.到达 4.到达终端网点)")
    private Integer status;
    @ApiModelProperty("发件人省份id")
    private String senderProvinceId;
    @ApiModelProperty("发件人城市id")
    private String senderCityId;
    @ApiModelProperty("发件人区县id")
    private String senderCountyId;
    @ApiModelProperty("发件人姓名")
    private String senderName;
    @ApiModelProperty("发件人电话")
    private String senderPhone;
    @ApiModelProperty("收件人省份id")
    private String receiverProvinceId;
    @ApiModelProperty("收件人城市id")
    private String receiverCityId;
    @ApiModelProperty("收件人姓名")
    private String receiverName;
    @ApiModelProperty("收件人区县id")
    private String receiverCountyId;
    @ApiModelProperty("收件人电话")
    private String receiverPhone;
}
