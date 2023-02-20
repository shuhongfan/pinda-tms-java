package com.itheima.pinda.vo.work;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.pinda.vo.base.angency.AgencySimpleVo;
import com.itheima.pinda.vo.base.userCenter.SysUserVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "司机作业单")
public class DriverJobVo implements Serializable {
    private static final long serialVersionUID = -4002290301421836423L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "起始机构")
    private AgencySimpleVo startAgency;

    @ApiModelProperty(value = "目的机构")
    private AgencySimpleVo endAgency;

    @ApiModelProperty(value = "作业状态，1为待执行（对应 待提货）、2为进行中（对应在途）、3为改派（对应 已交付）、4为已完成（对应 已交付）、5为已作废")
    private Integer status;

    @ApiModelProperty(value = "司机")
    private SysUserVo driver;

    @ApiModelProperty(value = "运输任务")
    private TaskTransportVo taskTransport;

    @ApiModelProperty(value = "提货对接人")
    private String startHandover;

    @ApiModelProperty(value = "交付对接人")
    private String finishHandover;

    @ApiModelProperty(value = "计划发车时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planDepartureTime;

    @ApiModelProperty(value = "实际发车时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualDepartureTime;

    @ApiModelProperty(value = "计划到达时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime planArrivalTime;

    @ApiModelProperty(value = "实际到达时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime actualArrivalTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @ApiModelProperty(value = "页码")
    private Integer page;

    @ApiModelProperty(value = "页尺寸")
    private Integer pageSize;
}
