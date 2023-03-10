package com.itheima.pinda.vo.base.transforCenter.business;

import com.itheima.pinda.vo.base.angency.AgencyVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "线路信息")
public class TransportLineVo implements Serializable {
    private static final long serialVersionUID = 5953547594480342286L;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "线路名称")
    private String name;
    @ApiModelProperty(value = "线路编号")
    private String lineNumber;
    @ApiModelProperty(value = "所属机构")
    private AgencyVo agency;
    @ApiModelProperty(value = "线路类型")
    private TransportLineTypeVo transportLineType;
    @ApiModelProperty(value = "起始地机构")
    private AgencyVo startAgency;
    @ApiModelProperty(value = "目的地机构")
    private AgencyVo endAgency;
    @ApiModelProperty(value = "距离")
    private BigDecimal distance;
    @ApiModelProperty(value = "成本")
    private BigDecimal cost;
    @ApiModelProperty(value = "预计时间")
    private BigDecimal estimatedTime;
}
