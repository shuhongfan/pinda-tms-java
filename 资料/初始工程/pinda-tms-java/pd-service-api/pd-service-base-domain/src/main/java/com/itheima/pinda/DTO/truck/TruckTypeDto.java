package com.itheima.pinda.DTO.truck;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * TruckTypeDto
 */
@Data
public class TruckTypeDto implements Serializable {
    private static final long serialVersionUID = -2762539095338170845L;
    /**
     * id
     */
    private String id;
    /**
     * 车辆类型名称
     */
    private String name;
    /**
     * 准载重量
     */
    private BigDecimal allowableLoad;
    /**
     * 准载体积
     */
    private BigDecimal allowableVolume;
    /**
     * 长
     */
    private BigDecimal measureLong;
    /**
     * 宽
     */
    private BigDecimal measureWidth;
    /**
     * 高
     */
    private BigDecimal measureHigh;
    /**
     * 货物类型id列表
     */
    private List<String> goodsTypeIds;
}