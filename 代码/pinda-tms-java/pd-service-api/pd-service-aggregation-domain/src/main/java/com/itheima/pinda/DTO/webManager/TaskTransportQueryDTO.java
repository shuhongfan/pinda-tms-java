package com.itheima.pinda.DTO.webManager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TaskTransportQueryDTO {
    @ApiModelProperty("当前页数")
    private Integer page = 1;
    @ApiModelProperty("每页条数")
    private Integer pageSize = 10;
    @ApiModelProperty("运输任务id")
    private String id;
    @ApiModelProperty("司机姓名")
    private String driverName;
    @ApiModelProperty("运输任务状态")
    private Integer status;
}
