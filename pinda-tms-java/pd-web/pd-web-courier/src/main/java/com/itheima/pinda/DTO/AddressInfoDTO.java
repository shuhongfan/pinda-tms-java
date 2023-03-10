package com.itheima.pinda.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AddressInfoDTO implements Serializable {
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 公司
     */
    @ApiModelProperty("公司")
    private String company;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phoneNumber;

}
