package com.itheima.pinda.vo.base.userCenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "密码重置")
@Data
public class PasswordResetVo implements Serializable {
    private static final long serialVersionUID = 3260392307623280638L;
    @ApiModelProperty(value = "原始密码")
    private String sourcePassword;
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
