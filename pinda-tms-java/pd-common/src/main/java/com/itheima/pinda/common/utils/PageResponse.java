package com.itheima.pinda.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 分页结果包装
 *
 * @author itcast
 */
@Data
@ApiModel(value = "分页数据消息体", description = "分页数据统一对象")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PageResponse<T> {

    @ApiModelProperty(value = "总条目数", required = true)
    private Long counts;

    @ApiModelProperty(value = "页尺寸", required = true)
    private Integer pagesize;

    @ApiModelProperty(value = "总页数", required = true)
    private Long pages;

    @ApiModelProperty(value = "页码", required = true)
    private Integer page;

    @ApiModelProperty(value = "数据列表", required = true)
    private List<T> items;
}
