package com.itheima.pinda.service;

import com.itheima.pinda.DTO.DriverJobDTO;
import com.itheima.pinda.DTO.TaskPickupDispatchDTO;
import com.itheima.pinda.DTO.TaskTransportDTO;
import com.itheima.pinda.DTO.TransportOrderDTO;
import com.itheima.pinda.DTO.webManager.DriverJobQueryDTO;
import com.itheima.pinda.DTO.webManager.TaskPickupDispatchQueryDTO;
import com.itheima.pinda.DTO.webManager.TaskTransportQueryDTO;
import com.itheima.pinda.DTO.webManager.TransportOrderQueryDTO;
import com.itheima.pinda.common.utils.PageResponse;

public interface WebManagerService {
    /**
     * 获取司机作业单分页数据
     *
     * @param dto 查询参数
     * @return 司机作业单分页数据
     */
    PageResponse<DriverJobDTO> findDriverJobByPage(DriverJobQueryDTO dto);

    /**
     * 获取取派件任务分页数据
     *
     * @param dto 查询参数
     * @return 取派件分页数据
     */
    PageResponse<TaskPickupDispatchDTO> findTaskPickupDispatchJobByPage(TaskPickupDispatchQueryDTO dto);

    /**
     * 获取运单分页数据
     *
     * @param dto 查询参数
     * @return 运单分页数据
     */
    PageResponse<TransportOrderDTO> findTransportOrderByPage(TransportOrderQueryDTO dto);

    /**
     * 获取运输任务分页数据
     *
     * @param dto 查询参数
     * @return 运输任务分页数据
     */
    PageResponse<TaskTransportDTO> findTaskTransportByPage(TaskTransportQueryDTO dto);
}
