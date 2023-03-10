package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pd_order")
@ApiModel
public class Order implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;


    /**
     * 订单类型，1为同城订单，2为城际订单
     */
    private Integer orderType;


    /**
     * 取件类型，1为同城订单，2为城际订单
     */
    private Integer pickupType;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 客户id
     */
    private String memberId;

    /**
     * 收件人省份id
     */
    private String receiverProvinceId;

    /**
     * 收件人城市id
     */
    private String receiverCityId;

    /**
     * 收件人区县id
     */
    private String receiverCountyId;

    /**
     * 收件人详细地址
     */
    private String receiverAddress;

    /**
     * 收件人地址Id
     */
    private String receiverAddressId;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人电话
     */
    private String receiverPhone;

    /**
     * 发件人省份id
     */
    private String senderProvinceId;

    /**
     * 发件人城市id
     */
    private String senderCityId;

    /**
     * 发件人区县id
     */
    private String senderCountyId;

    /**
     * 发件人详细地址
     */
    private String senderAddress;

    /**
     * 发件人地址Id
     */
    private String senderAddressId;

    /**
     * 发件人姓名
     */
    private String senderName;

    /**
     * 发件人电话
     */
    private String senderPhone;

    /**
     * 当前所在网点
     */
    private String currentAgencyId;

    /**
     * 付款方式,1.预结2到付
     */
    private Integer paymentMethod;

    /**
     * 付款状态,1.未付2已付
     */
    private Integer paymentStatus;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 预计到达时间
     */
    private LocalDateTime estimatedArrivalTime;

    /**
     * 距离
     */
    private BigDecimal distance;

    /**
     * 订单状态: 23000为待取件,23001为已取件，23002为网点自寄，23003为网点入库，
     * 23004为待装车，23005为运输中，23006为网点出库，23007为待派送，23008为派送中，
     * 23009为已签收，23010为拒收，230011为已取消
     */
    private Integer status;


}
