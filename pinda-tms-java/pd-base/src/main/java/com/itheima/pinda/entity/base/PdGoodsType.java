package com.itheima.pinda.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 货物类型
 */
@Data
@TableName("pd_goods_type")
public class PdGoodsType implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 货物类型名称
     */
    private String name;
    /**
     * 默认重量，单位：千克
     */
    private BigDecimal defaultWeight;
    /**
     * 默认体积，单位：方
     */
    private BigDecimal defaultVolume;
    /**
     * 说明
     */
    private String remark;
    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}