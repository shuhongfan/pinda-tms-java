package com.itheima.pinda.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author diesel
 * @since 2020-3-30
 * 地址簿
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AddressBook {

    private String id;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;
    /**
     * 名字
     */
    @ApiModelProperty("名字")
    private String name;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phoneNumber;
    /**
     * 分机号
     */
    @ApiModelProperty("分机号")
    private String extensionNumber;
    /**
     * 省id
     */
    @ApiModelProperty("省ID")
    private Long provinceId;
    /**
     * 市id
     */
    @ApiModelProperty("市ID")
    private Long cityId;
    /**
     * 区域id
     */
    @ApiModelProperty("区ID")
    private Long countyId;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;
    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String companyName;
    /**
     * 是否默认  1默认
     */
    @ApiModelProperty("是否默认  1默认")
    private Integer isDefault;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
