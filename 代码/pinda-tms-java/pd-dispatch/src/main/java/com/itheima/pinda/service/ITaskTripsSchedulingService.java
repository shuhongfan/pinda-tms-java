package com.itheima.pinda.service;

import com.itheima.pinda.DTO.OrderLineDTO;
import com.itheima.pinda.DTO.OrderLineSimpleDTO;
import com.itheima.pinda.DTO.OrderLineTripsTruckDriverDTO;

import java.util.List;

/**
 * 车辆调度
 */
public interface ITaskTripsSchedulingService {
    /**
     * 执行
     *
     * @param orderLineSimpleDTOS
     * @param businessId
     * @param jobId
     * @param logId
     * @return
     */
    List<OrderLineTripsTruckDriverDTO> execute(List<OrderLineSimpleDTO> orderLineSimpleDTOS, String businessId, String jobId, String logId, String agencyId);
}
