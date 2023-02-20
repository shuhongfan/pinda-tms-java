package com.itheima.pinda.DTO;

import com.itheima.pinda.entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class OrderLineTripsTruckDriverDTO {

    private TripsTruckDriverDTO tripsTruckDriverDTO;

    private String transportLineId;

    private List<OrderClassifyGroupDTO> orders;

    public OrderLineTripsTruckDriverDTO(TripsTruckDriverDTO tripsTruckDriverDTO, String transportLineId, List<OrderClassifyGroupDTO> orders) {
        this.tripsTruckDriverDTO = tripsTruckDriverDTO;
        this.transportLineId = transportLineId;
        this.orders = orders;
    }
}
