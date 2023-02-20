package com.itheima.pinda.vo.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "行政区域简要信息")
public class AreaSimpleVo implements Serializable {
    private static final long serialVersionUID = 5473514348905248093L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "行政名称")
    private String name;
    @ApiModelProperty(value = "经度")
    private String lng;
    @ApiModelProperty(value = "纬度")
    private String lat;
    /**
     * 多边形经纬度坐标集合
     */
    @ApiModelProperty(value = "多边形经纬度坐标集合")
    private List<List<Map>> mutiPoints;
}
