package com.itheima.pinda.DTO.transportline;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * TransportLineDto
 */
@Data
public class TransportLineDto implements Serializable{
    private static final long serialVersionUID = 1168526376000160339L;
    /**
     * id
     */
    private String id;
    /**
     * 线路名称
     */
    private String name;
    /**
     * 线路编号
     */
    private String lineNumber;
    /**
     * 所属机构id
     */
    private String agencyId;
    /**
     * 线路类型id
     */
    private String transportLineTypeId;
    /**
     * 起始地机构id
     */
    private String startAgencyId;
    /**
     * 目的地机构id
     */
    private String endAgencyId;
    /**
     * 距离
     */
    private BigDecimal distance;
    /**
     * 成本
     */
    private BigDecimal cost;
    /**
     * 预计时间
     */
    private BigDecimal estimatedTime;
    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}