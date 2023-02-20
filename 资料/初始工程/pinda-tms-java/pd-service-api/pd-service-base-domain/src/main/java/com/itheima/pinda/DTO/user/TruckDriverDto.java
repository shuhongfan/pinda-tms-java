package com.itheima.pinda.DTO.user;

import java.io.Serializable;

import lombok.Data;

/**
 * TruckDriverDto
 */
@Data
public class TruckDriverDto implements Serializable {
    private static final long serialVersionUID = 4960262265247824283L;
    /**
     * id
     */
    private String id;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 所属车队id
     */
    private String fleetId;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 照片
     */
    private String picture;
    /**
     * 驾龄
     */
    private Integer drivingAge;
}