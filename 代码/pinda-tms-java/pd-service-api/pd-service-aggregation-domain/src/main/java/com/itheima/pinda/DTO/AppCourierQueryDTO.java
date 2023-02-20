package com.itheima.pinda.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppCourierQueryDTO {
    @ApiModelProperty("当前页数")
    private Integer page = 1;
    @ApiModelProperty("每页条数")
    private Integer pageSize = 10;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("搜索条件")
    private String keyword;
    @ApiModelProperty("任务类型")
    private Integer taskType;
    @ApiModelProperty("历史时间")
    private String date;
    @ApiModelProperty("快递员id")
    private String courierId;
}
