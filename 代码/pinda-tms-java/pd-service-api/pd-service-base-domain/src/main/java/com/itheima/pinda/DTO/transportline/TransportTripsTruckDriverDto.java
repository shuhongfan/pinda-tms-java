package com.itheima.pinda.DTO.transportline;

import lombok.Data;

import java.io.Serializable;

/**
 * TransportTripsDto
 */
@Data
public class TransportTripsTruckDriverDto implements Serializable {
    private static final long serialVersionUID = -477835165829525987L;
    /**
     * id
     */
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
     * 司机Id
     */
    private String userId;
}