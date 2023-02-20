package com.itheima.pinda.DTO.transportline;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * TransportLineType
 */
@Data
public class TransportLineTypeDto implements Serializable {
    private static final long serialVersionUID = -7006861734336133221L;
    /**
     * id
     */
    private String id;
    /**
     * 线路类型名称
     */
    private String name;
    /**
     * 线路类型编码
     */
    private String typeNumber;
    /**
     * 起始地机构类型
     */
    private Integer startAgencyType;
    /**
     * 目的地机构类型
     */
    private Integer endAgencyType;
    /**
     * 最后更新时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime lastUpdateTime;
    /**
     * 最后更新人id
     */
    private String updater;
    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}