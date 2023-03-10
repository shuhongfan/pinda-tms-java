package com.itheima.pinda.DTO;

import lombok.Data;

import java.util.List;

@Data
public class TransportOrderSearchDTO extends TransportOrderDTO {


    /**
     * 订单ID 集合
     */
    private List<String> orderIds;

}
