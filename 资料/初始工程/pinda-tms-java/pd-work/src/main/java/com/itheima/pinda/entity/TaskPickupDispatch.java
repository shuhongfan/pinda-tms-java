package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 取件、派件任务信息表
 * </p>
 *
 * @author jpf
 * @since 2020-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pd_task_pickup_dispatch")
public class TaskPickupDispatch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    /**
     * 关联订单id
     */
    private String orderId;

    /**
     * 任务类型，1为取件任务，2为派件任务
     */
    private Integer taskType;

    /**
     * 任务状态，1为待执行（对应 待上门和须交接）、2为进行中（该状态暂不使用，属于保留状态）、3为待确认（对应 待妥投和须交件）、4为已完成、5为已取消
     */
    private Integer status;

    /**
     * 签收状态(1为已签收，2为拒收)
     */
    private Integer signStatus;

    /**
     * 网点ID
     */
    private String agencyId;

    /**
     * 快递员ID
     */
    private String courierId;

    /**
     * 预计开始时间
     */
    private LocalDateTime estimatedStartTime;

    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;

    /**
     * 预计完成时间
     */
    private LocalDateTime estimatedEndTime;

    /**
     * 实际完成时间
     */
    private LocalDateTime actualEndTime;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 任务分配状态(1未分配2已分配3待人工分配)
     */
    private Integer assignedStatus;

    /**
     * 备注
     */
    private String mark;

    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;
}
