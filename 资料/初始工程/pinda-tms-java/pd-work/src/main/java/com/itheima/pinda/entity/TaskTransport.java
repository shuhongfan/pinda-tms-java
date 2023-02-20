package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 运输任务表
 * </p>
 *
 * @author jpf
 * @since 2020-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pd_task_transport")
public class TaskTransport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 车次id
     */
    private String transportTripsId;

    /**
     * 起始机构id
     */
    private String startAgencyId;

    /**
     * 目的机构id
     */
    private String endAgencyId;

    /**
     * 任务状态，1为待执行（对应 待提货）、2为进行中（对应在途）、3为待确认（保留状态）、4为已完成（对应 已交付）、5为已取消
     */
    private Integer status;

    /**
     * 任务分配状态(1未分配2已分配3待人工分配)
     */
    private Integer assignedStatus;

    /**
     * 满载状态(1.半载2.满载3.空载)
     */
    private Integer loadingStatus;

    /**
     * 车辆id
     */
    private String truckId;

    /**
     * 提货凭证
     */
    private String cargoPickUpPicture;

    /**
     * 货物照片
     */
    private String cargoPicture;

    /**
     * 运回单凭证
     */
    private String transportCertificate;

    /**
     * 计划发车时间
     */
    private LocalDateTime planDepartureTime;

    /**
     * 实际发车时间
     */
    private LocalDateTime actualDepartureTime;

    /**
     * 计划到达时间
     */
    private LocalDateTime planArrivalTime;

    /**
     * 实际到达时间
     */
    private LocalDateTime actualArrivalTime;

    /**
     * 计划提货时间
     */
    private LocalDateTime planPickUpGoodsTime;

    /**
     * 实际提货时间
     */
    private LocalDateTime actualPickUpGoodsTime;

    /**
     * 计划交付时间
     */
    private LocalDateTime planDeliveryTime;

    /**
     * 实际交付时间
     */
    private LocalDateTime actualDeliveryTime;

    /**
     * 交付货物照片
     */
    private String deliverPicture;
    /**
     * 提货纬度
     */
    private String deliveryLatitude;
    /**
     * 提货经度
     */
    private String deliveryLongitude;
    /**
     * 交付纬度
     */
    private String deliverLatitude;
    /**
     * 交付经度
     */
    private String deliverLongitude;

    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;
}
