package com.itheima.pinda.feign.webManager;

import com.itheima.pinda.DTO.DriverJobDTO;
import com.itheima.pinda.DTO.TaskPickupDispatchDTO;
import com.itheima.pinda.DTO.TaskTransportDTO;
import com.itheima.pinda.DTO.TransportOrderDTO;
import com.itheima.pinda.DTO.webManager.DriverJobQueryDTO;
import com.itheima.pinda.DTO.webManager.TaskPickupDispatchQueryDTO;
import com.itheima.pinda.DTO.webManager.TaskTransportQueryDTO;
import com.itheima.pinda.DTO.webManager.TransportOrderQueryDTO;
import com.itheima.pinda.common.utils.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient(name = "pd-aggregation")
@RequestMapping("webManager")
@ApiIgnore
public interface WebManagerFeign {
    /**
     * 分页查询司机任务
     *
     * @param dto 查询参数
     * @return 司机任务分页数据
     */
    @PostMapping("driverJob/page")
    PageResponse<DriverJobDTO> findDriverJobByPage(@RequestBody DriverJobQueryDTO dto);

    /**
     * 分页查询取派件任务
     *
     * @param dto 查询参数
     * @return 取派件任务分页数据
     */
    @PostMapping("taskPickupDispatchJob/page")
    PageResponse<TaskPickupDispatchDTO> findTaskPickupDispatchJobByPage(@RequestBody TaskPickupDispatchQueryDTO dto);

    /**
     * 分页查询运单信息
     *
     * @param dto 查询参数
     * @return 运单分页数据
     */
    @PostMapping("transportOrder/page")
    PageResponse<TransportOrderDTO> findTransportOrderByPage(@RequestBody TransportOrderQueryDTO dto);

    /**
     * 分页查询运输任务信息
     *
     * @param dto 查询参数
     * @return 运输任务分页数据
     */
    @PostMapping("taskTransport/page")
    PageResponse<TaskTransportDTO> findTaskTransportByPage(@RequestBody TaskTransportQueryDTO dto);
}
