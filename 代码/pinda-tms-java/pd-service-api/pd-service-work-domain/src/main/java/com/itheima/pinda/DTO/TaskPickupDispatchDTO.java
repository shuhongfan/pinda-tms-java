package com.itheima.pinda.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 取派件任务
 *
 * @author jpfss
 */
@Data
public class TaskPickupDispatchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
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
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime estimatedStartTime;

    /**
     * 实际开始时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualStartTime;

    /**
     * 预计完成时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime estimatedEndTime;

    /**
     * 实际完成时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualEndTime;

    /**
     * 确认时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime confirmTime;

    /**
     * 取消时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
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
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 页尺寸
     */
    private Integer pageSize;

    /**
     * 发件人省份id
     */
    private String senderProvinceId;

    /**
     * 发件人城市id
     */
    private String senderCityId;

    /**
     * 发件人姓名
     */
    private String senderName;

    /**
     * 收件人省份id
     */
    private String receiverProvinceId;

    /**
     * 收件人城市id
     */
    private String receiverCityId;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * id列表
     */
    private List<String> ids;
    /**
     * orderId 列表
     */
    private List<String> orderIds;
}
