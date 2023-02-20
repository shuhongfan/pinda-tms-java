package com.itheima.pinda.vo.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.pinda.vo.oms.OrderVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(value = "运单信息")
public class TransportOrderVo implements Serializable {
    private static final long serialVersionUID = 208202200909848030L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "订单信息")
    private OrderVo order;

    @ApiModelProperty(value = "运单状态(1.新建 2.已装车，发往x转运中心 3.到达 4.到达终端网点)")
    private Integer status;

    @ApiModelProperty(value = "调度状态调度状态(1.待调度2.未匹配线路3.已调度)")
    private Integer schedulingStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "页尺寸")
    private Integer pageSize;

    @ApiModelProperty(value = "取件信息")
    private TaskPickupDispatchVo taskPickup;

    @ApiModelProperty(value = "派件信息")
    private TaskPickupDispatchVo taskDispatch;

    @ApiModelProperty(value = "运输信息")
    private List<TaskTransportVo> taskTransports;
}
