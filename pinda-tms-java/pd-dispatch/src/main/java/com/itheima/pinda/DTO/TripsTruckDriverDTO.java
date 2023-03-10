package com.itheima.pinda.DTO;

import lombok.Data;

@Data
public class TripsTruckDriverDTO {
    private String tripsId;
    private String truckId;
    private String driverId;

    public TripsTruckDriverDTO(String tripsId, String truckId, String driverId) {
        this.tripsId = tripsId;
        this.truckId = truckId;
        this.driverId = driverId;
    }
}
