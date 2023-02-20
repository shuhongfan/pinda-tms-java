package com.itheima.pinda.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class CustomerOrderDetailDTO {
    /**
     * 订单号
     */
    private String id;
    /**
     * 订单状态
     */
    private int status;
    /**
     * 运单号
     */
    private String tranOrderId;
    /**
     * 计划取件时间
     */
    private LocalDateTime planPickUpTime;

    /**
     * 派送完成时间
     */
    private LocalDateTime actualDispathedTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 头像
     */
    private String avatar;
}
