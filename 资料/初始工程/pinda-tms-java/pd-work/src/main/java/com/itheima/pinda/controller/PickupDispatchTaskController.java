package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.DTO.TaskPickupDispatchDTO;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.entity.TaskPickupDispatch;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskAssignedStatus;
import com.itheima.pinda.service.ITaskPickupDispatchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * 取件、派件任务
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("pickup-dispatch-task")
public class PickupDispatchTaskController {
    @Autowired
    private ITaskPickupDispatchService taskPickupDispatchService;

    /**
     * 新增取派件任务
     *
     * @param dto 取派件任务信息
     * @return 取派件任务信息
     */
    @PostMapping("")
    public TaskPickupDispatchDTO save(@RequestBody TaskPickupDispatchDTO dto) {
        TaskPickupDispatch dispatch = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, dispatch);
        log.info("新增取派件任务:{}    {}", dto, dispatch);
        taskPickupDispatchService.saveTaskPickupDispatch(dispatch);
        TaskPickupDispatchDTO result = new TaskPickupDispatchDTO();
        BeanUtils.copyProperties(dispatch, result);
        return result;
    }

    /**
     * 修改取派件任务信息
     *
     * @param id  取派件任务id
     * @param dto 取派件任务信息
     * @return 取派件任务信息
     */
    @PutMapping("/{id}")
    public TaskPickupDispatchDTO updateById(@PathVariable(name = "id") String id, @RequestBody TaskPickupDispatchDTO dto) {
        dto.setId(id);
        TaskPickupDispatch dispatch = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, dispatch);
        if (StringUtils.isNotEmpty(dispatch.getCourierId())) {
            dispatch.setAssignedStatus(PickupDispatchTaskAssignedStatus.DISTRIBUTED.getCode());
        }
        taskPickupDispatchService.updateById(dispatch);
        return dto;
    }

    /**
     * 获取取派件任务分页数据
     *
     * @param dto 查询条件
     * @return 取派件分页数据
     */
    @PostMapping("/page")
    public PageResponse<TaskPickupDispatchDTO> findByPage(@RequestBody TaskPickupDispatchDTO dto) {
        if (dto.getPage() == null) {
            dto.setPage(1);
        }
        if (dto.getPageSize() == null) {
            dto.setPageSize(10);
        }
        TaskPickupDispatch queryTask = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, queryTask);
        IPage<TaskPickupDispatch> orderIPage = taskPickupDispatchService.findByPage(dto.getPage(), dto.getPageSize(), queryTask);
        List<TaskPickupDispatchDTO> dtoList = new ArrayList<>();
        orderIPage.getRecords().forEach(order -> {
            TaskPickupDispatchDTO resultDto = new TaskPickupDispatchDTO();
            BeanUtils.copyProperties(order, resultDto);
            dtoList.add(resultDto);
        });
        return PageResponse.<TaskPickupDispatchDTO>builder().items(dtoList).pagesize(dto.getPageSize()).page(dto.getPage()).counts(orderIPage.getTotal())
                .pages(orderIPage.getPages()).build();
    }

    /**
     * 获取取派件任务列表
     *
     * @param dto 查询条件
     * @return 取派件任务列表
     */
    @PostMapping("/list")
    public List<TaskPickupDispatchDTO> findAll(@RequestBody TaskPickupDispatchDTO dto) {
        TaskPickupDispatch queryTask = new TaskPickupDispatch();
        BeanUtils.copyProperties(dto, queryTask);
        return taskPickupDispatchService.findAll(dto.getIds(), dto.getOrderIds(), queryTask).stream().map(taskPickupDispatch -> {
            TaskPickupDispatchDTO resultDto = new TaskPickupDispatchDTO();
            BeanUtils.copyProperties(taskPickupDispatch, resultDto);
            return resultDto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据id获取取派件任务信息
     *
     * @param id 任务Id
     * @return 任务详情
     */
    @GetMapping("/{id}")
    public TaskPickupDispatchDTO findById(@PathVariable(name = "id") String id) {
        TaskPickupDispatchDTO dto = new TaskPickupDispatchDTO();
        TaskPickupDispatch dispatch = taskPickupDispatchService.getById(id);
        if (dispatch != null) {
            BeanUtils.copyProperties(dispatch, dto);
        } else {
            dto = null;
        }
        return dto;
    }

    /**
     * 根据订单id获取取派件任务信息
     *
     * @param orderId 订单Id
     * @return 任务详情
     */
    @GetMapping("/orderId/{orderId}/{taskType}")
    public TaskPickupDispatchDTO findByOrderId(@PathVariable("orderId") String orderId, @PathVariable("taskType") Integer taskType) {
        TaskPickupDispatchDTO dto = new TaskPickupDispatchDTO();

        LambdaQueryWrapper<TaskPickupDispatch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TaskPickupDispatch::getOrderId, orderId);
        wrapper.eq(TaskPickupDispatch::getTaskType, taskType);
        TaskPickupDispatch dispatch = taskPickupDispatchService.getOne(wrapper);
        if (dispatch != null) {
            BeanUtils.copyProperties(dispatch, dto);
        } else {
            dto = null;
        }
        return dto;
    }

}
