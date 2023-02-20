package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单位置信息
 */
@Data
@TableName("pd_order_location")
public class OrderLocation implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 发送地址坐标
     */
    private String sendLocation;
    /**
     * 收货地址坐标
     */
    private String receiveLocation;
    /**
     * 发送起始网点
     */
    private String sendAgentId;

    /**
     * 接受的终止网点
     */
    private String receiveAgentId;
    /**
     * 记录状态 0：无效，1有效
     */
    private String status;

}