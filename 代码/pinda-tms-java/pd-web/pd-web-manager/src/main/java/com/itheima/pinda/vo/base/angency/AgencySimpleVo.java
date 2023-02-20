package com.itheima.pinda.vo.base.angency;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "机构简要信息")
public class AgencySimpleVo implements Serializable {
    private static final long serialVersionUID = -6300342950882936227L;
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "子部门简要信息列表")
    private List<AgencySimpleVo> subAgencies;
}
