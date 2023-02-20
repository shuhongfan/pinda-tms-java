package com.itheima.pinda.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 3355577312030835991L;
    /**
     * id
     */
    private String id;

    /**
     * 订单类型，1为同城订单，2为城际订单
     */
    private Integer orderType;


    /**
     * 取件类型，1为网点自寄，2为上门取件
     */
    private Integer pickupType;

    /**
     * 下单时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
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
     * 收件人地址id
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
     * 发件人地址id
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

    /**
     * 页码
     */
    private Integer page;

    /**
     * 页尺寸
     */
    private Integer pageSize;

    private OrderCargoDto orderCargoDto;
}
