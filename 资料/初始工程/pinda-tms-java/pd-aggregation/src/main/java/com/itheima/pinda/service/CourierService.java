package com.itheima.pinda.service;

import com.itheima.pinda.DTO.AppCourierQueryDTO;
import com.itheima.pinda.DTO.TaskPickupDispatchDTO;
import com.itheima.pinda.common.utils.PageResponse;

public interface CourierService {
    PageResponse<TaskPickupDispatchDTO> findByPage(AppCourierQueryDTO dto);
}
