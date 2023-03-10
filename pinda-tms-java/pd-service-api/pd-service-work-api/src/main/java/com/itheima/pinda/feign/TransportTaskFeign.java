package com.itheima.pinda.feign;

import com.itheima.pinda.DTO.TaskTransportDTO;
import com.itheima.pinda.common.utils.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("pd-work")
@RequestMapping("transport-task")
public interface TransportTaskFeign {
    /**
     * 新增运输任务
     *
     * @param dto 运输任务信息
     * @return 运输任务信息
     */
    @PostMapping("")
    TaskTransportDTO save(@RequestBody TaskTransportDTO dto);

    /**
     * 修改运输任务信息
     *
     * @param id  运输任务id
     * @param dto 运输任务信息
     * @return 运输任务信息
     */
    @PutMapping("/{id}")
    TaskTransportDTO updateById(@PathVariable(name = "id") String id, @RequestBody TaskTransportDTO dto);

    /**
     * 获取运输任务分页数据
     *
     * @param dto 查询参数
     * @return 运输任务分页数据
     */
    @PostMapping("/page")
    PageResponse<TaskTransportDTO> findByPage(@RequestBody TaskTransportDTO dto);

    /**
     * 根据id获取运输任务信息
     *
     * @param id 运输任务id
     * @return 运输任务信息
     */
    @GetMapping("/{id}")
    TaskTransportDTO findById(@PathVariable(name = "id") String id);

    /**
     * 获取运单列表
     *
     * @param dto 查询条件
     * @return 运单列表
     */
    @PostMapping("/list")
    List<TaskTransportDTO> findAll(@RequestBody TaskTransportDTO dto);

    /**
     * 根据运单id或运输任务id获取运输任务列表
     *
     * @return 运输任务列表
     */
    @GetMapping("/listByOrderIdOrTaskId")
    List<TaskTransportDTO> findAllByOrderIdOrTaskId(@RequestParam(name = "transportOrderId", required = false) String transportOrderId,
                                                    @RequestParam(name = "taskTransportId", required = false) String taskTransportId);
}
