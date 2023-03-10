package com.itheima.pinda.DTO;

import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.entity.Member;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
public class PickupDispatchDetailDTO implements Serializable {
    /**
     * 取件任务主键
     */
    @ApiModelProperty("主键")
    private String id;
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderNumber;
    /**
     * 运单号
     */
    @ApiModelProperty("运单号")
    private String tranOrderId;

    /**
     * 物品类型
     */
    @ApiModelProperty("物品类型")
    private String goodsTypeId;

    /**
     * 物品类型名称
     */
    @ApiModelProperty("物品类型名称")
    private String goodsTypeName;

    /**
     * 收件人
     */
    @ApiModelProperty("收件人")
    private AddressInfoDTO receiver;

    /**
     * 发件人
     */
    @ApiModelProperty("发件人")
    private AddressInfoDTO sender;

    /**
     * 寄托物
     */
    @ApiModelProperty("寄托物")
    private String sustenance;

    /**
     * 重量
     */
    @ApiModelProperty("重量")
    private BigDecimal weight;

    /**
     * 体积
     */
    @ApiModelProperty("体积")
    private BigDecimal volume;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private Integer quantity;

    /**
     * 付款方式
     */
    @ApiModelProperty("付款方式")
    private Integer paymentMethod;

    /**
     *
     */
    @ApiModelProperty("运费合计")
    private String amount;

    @ApiModelProperty("身份证号是否验证  1 已验证 其他未验证")
    private int idCardNoVerify;

    public PickupDispatchDetailDTO(TaskPickupDispatchDTO pickupDispatchTaskDTO, OrderDTO orderInfoDTO, OrderCargoDto orderCargoDto, GoodsTypeDto goodsType, Map<Long, Area> areaMap, TransportOrderDTO transportOrder, Member member) {
        this.id = pickupDispatchTaskDTO.getId();
        this.amount = orderInfoDTO.getAmount() == null ? "0.00" : orderInfoDTO.getAmount().toString();
        this.orderNumber = pickupDispatchTaskDTO.getOrderId();
        this.tranOrderId = transportOrder == null ? "" : transportOrder.getId();
        this.goodsTypeId = orderCargoDto.getGoodsTypeId();
        this.goodsTypeName = goodsType != null ? goodsType.getName() : "";
        this.sustenance = orderCargoDto.getName();
        this.weight = orderCargoDto.getWeight() == null ? BigDecimal.ZERO : orderCargoDto.getWeight();
        this.volume = orderCargoDto.getVolume() == null ? BigDecimal.ZERO : orderCargoDto.getVolume();
        this.quantity = orderCargoDto.getQuantity();
        this.paymentMethod = orderInfoDTO.getPaymentMethod();
        this.idCardNoVerify = member != null ? member.getIdCardNoVerify() : 0;

        StringBuffer senderAddress = new StringBuffer();
        senderAddress.append(areaMap.containsKey(parseLong(orderInfoDTO.getSenderProvinceId())) ? areaMap.get(parseLong(orderInfoDTO.getSenderProvinceId())).getName() : "");
        senderAddress.append(areaMap.containsKey(parseLong(orderInfoDTO.getSenderCityId())) ? areaMap.get(parseLong(orderInfoDTO.getSenderCityId())).getName() : "");
        senderAddress.append(areaMap.containsKey(parseLong(orderInfoDTO.getSenderCountyId())) ? areaMap.get(parseLong(orderInfoDTO.getSenderCountyId())).getName() : "");
        senderAddress.append(orderInfoDTO.getSenderAddress());
        this.sender = AddressInfoDTO.builder().name(orderInfoDTO.getSenderName()).phoneNumber(orderInfoDTO.getSenderPhone()).address(senderAddress.toString()).build();


        StringBuffer receiverAddress = new StringBuffer();
        receiverAddress.append(areaMap.containsKey(parseLong(orderInfoDTO.getReceiverProvinceId())) ? areaMap.get(parseLong(orderInfoDTO.getReceiverProvinceId())).getName() : "");
        receiverAddress.append(areaMap.containsKey(parseLong(orderInfoDTO.getReceiverCityId())) ? areaMap.get(parseLong(orderInfoDTO.getReceiverCityId())).getName() : "");
        receiverAddress.append(areaMap.containsKey(parseLong(orderInfoDTO.getReceiverCountyId())) ? areaMap.get(parseLong(orderInfoDTO.getReceiverCountyId())).getName() : "");
        receiverAddress.append(orderInfoDTO.getReceiverAddress());
        this.receiver = AddressInfoDTO.builder().name(orderInfoDTO.getReceiverName()).phoneNumber(orderInfoDTO.getReceiverPhone()).address(receiverAddress.toString()).build();
    }


    private Long parseLong(String value) {
        if (StringUtils.isNotBlank(value)) {
            return Long.parseLong(value);
        }
        return null;
    }
}
