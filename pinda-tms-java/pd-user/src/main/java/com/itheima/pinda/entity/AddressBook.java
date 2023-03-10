package com.itheima.pinda.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 地址簿
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pd_address_book")
public class AddressBook implements Serializable{
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 名字
     */
    private String name;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 分机号
     */
    private String extensionNumber;
    /**
     * 省id
     */
    private Long provinceId;
    /**
     * 市id
     */
    private Long cityId;
    /**
     * 区域id
     */
    private Long countyId;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 是否默认  1默认
     */
    private Integer isDefault;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

}