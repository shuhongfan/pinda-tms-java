package com.itheima.pinda.feign;

import com.itheima.pinda.DTO.TaskPickupDispatchDTO;
import com.itheima.pinda.common.utils.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("pd-work")
@RequestMapping("pickup-dispatch-task")
public interface PickupDispatchTaskFeign {
    /**
     * 新增取派件任务
     *
     * @param dto 取派件任务信息
     * @return 取派件任务信息
     */
    @PostMapping("")
    TaskPickupDispatchDTO save(@RequestBody TaskPickupDispatchDTO dto);

    /**
     * 修改取派件任务信息
     *
     * @param id  取派件任务id
     * @param dto 取派件任务信息
     * @return 取派件任务信息
     */
    @PutMapping("/{id}")
    TaskPickupDispatchDTO updateById(@PathVariable(name = "id") String id, @RequestBody TaskPickupDispatchDTO dto);

    /**
     * 获取取派件任务分页数据
     *
     * @param dto 查询条件
     * @return 取派件分页数据
     */
    @PostMapping("/page")
    PageResponse<TaskPickupDispatchDTO> findByPage(@RequestBody TaskPickupDispatchDTO dto);

    /**
     * 根据id获取取派件任务信息
     *
     * @param id 任务Id
     * @return 任务详情
     */
    @GetMapping("/{id}")
    TaskPickupDispatchDTO findById(@PathVariable(name = "id") String id);

    /**
     * 获取取派件任务列表
     *
     * @param dto 查询条件
     * @return 取派件任务列表
     */
    @PostMapping("/list")
    List<TaskPickupDispatchDTO> findAll(@RequestBody TaskPickupDispatchDTO dto);

    /**
     * 根据订单id获取取派件任务信息
     *
     * @param orderId 订单Id
     * @return 任务详情
     */
    @GetMapping("/orderId/{orderId}/{taskType}")
    TaskPickupDispatchDTO findByOrderId(@PathVariable("orderId") String orderId, @PathVariable("taskType") Integer taskType);
}
