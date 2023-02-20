package com.itheima.pinda.entity.transportline;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 车次与车辆关联表
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
@Data
@TableName("pd_transport_trips_truck_driver")
public class PdTransportTripsTruckDriver implements Serializable {
    private static final long serialVersionUID = 2060686653575483040L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 车辆id
     */
    private String truckId;
    /**
     * 车次id
     */
    private String transportTripsId;
    /**
     * 司机id
     */
    private String userId;
}
