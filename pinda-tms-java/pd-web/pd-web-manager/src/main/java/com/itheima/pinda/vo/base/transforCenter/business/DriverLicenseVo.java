package com.itheima.pinda.vo.base.transforCenter.business;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "司机驾驶证信息")
public class DriverLicenseVo implements Serializable {
    private static final long serialVersionUID = 7204484845857308415L;
    @ApiModelProperty(value = "司机id")
    private String userId;
    @ApiModelProperty(value = "准驾车型")
    private String allowableType;
    @ApiModelProperty(value = "初次领证日期")
    private String initialCertificateDate;
    @ApiModelProperty(value = "有效期限")
    private String validPeriod;
    @ApiModelProperty(value = "驾驶证号")
    private String licenseNumber;
    @ApiModelProperty(value = "驾龄")
    private Integer driverAge;
    @ApiModelProperty(value = "驾驶证类型")
    private String licenseType;
    @ApiModelProperty(value = "从业资格证信息")
    private String qualificationCertificate;
    @ApiModelProperty(value = "入场证信息")
    private String passCertificate;
    @ApiModelProperty(value = "图片")
    private String picture;
}
