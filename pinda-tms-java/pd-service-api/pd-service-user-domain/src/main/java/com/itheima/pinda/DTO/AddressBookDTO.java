package com.itheima.pinda.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itheima.pinda.entity.AddressBook;
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
public class AddressBookDTO extends AddressBook {

    /**
     * 省
     */
    @ApiModelProperty("省")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty("市")
    private String city;
    /**
     * 区域
     */
    @ApiModelProperty("区")
    private String county;
    /**
     * 全地址
     */
    @ApiModelProperty("全地址")
    private String fullAddress;

}
