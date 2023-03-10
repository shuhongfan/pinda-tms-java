package com.itheima.pinda.entity.transportline;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 车次信息表
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Data
@TableName("pd_transport_trips")
public class PdTransportTrips implements Serializable {
    private static final long serialVersionUID = -934311173866081843L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 车次名称
     */
    private String name;

    /**
     * 发车时间
     */
    private String departureTime;

    /**
     * 所属线路id
     */
    private String transportLineId;

    /**
     * 周期，1为天，2为周，3为月
     */
    private Integer period;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;
}
