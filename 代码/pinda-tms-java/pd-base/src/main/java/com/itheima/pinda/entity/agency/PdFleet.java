package com.itheima.pinda.entity.agency;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 车队表
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Data
@TableName("pd_fleet")
public class PdFleet implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 车队名称
     */
    private String name;

    /**
     * 车队编号
     */
    private String fleetNumber;

    /**
     * 所属机构
     */
    private String agencyId;

    /**
     * 负责人
     */
    private String manager;
    /**
     * 状态 0：禁用 1：正常
     */
    private Integer status;
}
