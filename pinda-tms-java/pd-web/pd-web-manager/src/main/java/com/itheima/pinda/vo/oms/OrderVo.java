package com.itheima.pinda.vo.oms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.pinda.vo.base.AreaSimpleVo;
import com.itheima.pinda.vo.work.TaskPickupDispatchVo;
import com.itheima.pinda.vo.work.TransportOrderVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "订单信息")
public class OrderVo implements Serializable {
    private static final long serialVersionUID = 1713530076914843839L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "订单类型，1为同城订单，2为城际订单")
    private Integer orderType;

    @ApiModelProperty(value = "取件类型，1为网点自寄，2为上门取件")
    private Integer pickupType;

    @ApiModelProperty(value = "下单时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    // TODO: 2020/4/1 此处应修改为客户模型
    @ApiModelProperty(value = "客户id")
    private String memberId;

    @ApiModelProperty(value = "收件人省份")
    private AreaSimpleVo receiverProvince;

    @ApiModelProperty(value = "收件人城市")
    private AreaSimpleVo receiverCity;

    @ApiModelProperty(value = "收件人区县")
    private AreaSimpleVo receiverCounty;

    @ApiModelProperty(value = "收件人详细地址")
    private String receiverAddress;

    @ApiModelProperty(value = "收件人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收件人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "发件人省份")
    private AreaSimpleVo senderProvince;

    @ApiModelProperty(value = "发件人城市")
    private AreaSimpleVo senderCity;

    @ApiModelProperty(value = "发件人区县")
    private AreaSimpleVo senderCounty;

    @ApiModelProperty(value = "发件人详细地址")
    private String senderAddress;

    @ApiModelProperty(value = "发件人姓名")
    private String senderName;

    @ApiModelProperty(value = "发件人电话")
    private String senderPhone;

    @ApiModelProperty(value = "付款方式,1.预结2到付")
    private Integer paymentMethod;

    @ApiModelProperty(value = "付款状态,1.未付2已付")
    private Integer paymentStatus;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "预计到达时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime estimatedArrivalTime;

    @ApiModelProperty(value = "距离，单位：公里")
    private BigDecimal distance;

    @ApiModelProperty(value = "订单状态: 23000为待取件,23001为已取件，23002为网点自寄，" +
            "23003为网点入库，23004为待装车，23005为运输中，23006为网点出库，23007为待派送，" +
            "23008为派送中，23009为已签收，23010为拒收，230011为已取消")
    private Integer status;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "页尺寸")
    private Integer pageSize;

    @ApiModelProperty(value = "取件信息")
    private TaskPickupDispatchVo taskPickup;

    @ApiModelProperty(value = "派件信息")
    private TaskPickupDispatchVo taskDispatch;

    @ApiModelProperty(value = "运单信息")
    private TransportOrderVo transportOrder;
}
