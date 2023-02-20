package com.itheima.pinda.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TransportOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;


    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 运单状态(1.新建 2.已装车，发往x转运中心 3.到达 4.到达终端网点)
     */
    private Integer status;

    /**
     * 调度状态调度状态(1.待调度2.未匹配线路3.已调度)
     */
    private Integer schedulingStatus;

    /**
     * 创建时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;
}
