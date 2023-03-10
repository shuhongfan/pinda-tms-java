package com.itheima.pinda.entity.transportline;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 线路类型表
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Data
@TableName("pd_transport_line_type")
public class PdTransportLineType implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
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
    private LocalDateTime lastUpdateTime;

    /**
     * 最后更新人
     */
    private String updater;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;
}
