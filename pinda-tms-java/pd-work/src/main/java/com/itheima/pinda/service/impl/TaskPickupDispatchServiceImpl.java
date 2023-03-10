package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.TaskPickupDispatch;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskAssignedStatus;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskStatus;
import com.itheima.pinda.mapper.TaskPickupDispatchMapper;
import com.itheima.pinda.service.ITaskPickupDispatchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 取件、派件任务信息表 服务实现类
 * </p>
 *
 * @author jpf
 * @since 2019-12-30
 */
@Service
public class TaskPickupDispatchServiceImpl extends ServiceImpl<TaskPickupDispatchMapper, TaskPickupDispatch> implements ITaskPickupDispatchService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public TaskPickupDispatch saveTaskPickupDispatch(TaskPickupDispatch taskPickupDispatch) {
        taskPickupDispatch.setId(idGenerator.nextId(taskPickupDispatch) + "");
        taskPickupDispatch.setCreateTime(LocalDateTime.now());
        taskPickupDispatch.setStatus(PickupDispatchTaskStatus.PENDING.getCode());
        taskPickupDispatch.setAssignedStatus(PickupDispatchTaskAssignedStatus.TO_BE_DISTRIBUTED.getCode());
        save(taskPickupDispatch);
        return taskPickupDispatch;
    }

    @Override
    public IPage<TaskPickupDispatch> findByPage(Integer page, Integer pageSize, TaskPickupDispatch dispatch) {
        Page<TaskPickupDispatch> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<TaskPickupDispatch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dispatch.getCourierId())) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getCourierId, dispatch.getCourierId());
        }
        if (dispatch.getAssignedStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getAssignedStatus, dispatch.getAssignedStatus());
        }
        if (dispatch.getTaskType() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getTaskType, dispatch.getTaskType());
        }
        if (dispatch.getStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getStatus, dispatch.getStatus());
        }
        lambdaQueryWrapper.orderBy(true, false, TaskPickupDispatch::getId);
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<TaskPickupDispatch> findAll(List<String> ids, List<String> orderIds, TaskPickupDispatch dispatch) {
        LambdaQueryWrapper<TaskPickupDispatch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(TaskPickupDispatch::getId, ids);
        }
        if (orderIds != null && orderIds.size() > 0) {
            lambdaQueryWrapper.in(TaskPickupDispatch::getOrderId, orderIds);
        }
        if (dispatch.getAssignedStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getAssignedStatus, dispatch.getAssignedStatus());
        }
        if (dispatch.getTaskType() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getTaskType, dispatch.getTaskType());
        }
        if (dispatch.getStatus() != null) {
            lambdaQueryWrapper.eq(TaskPickupDispatch::getStatus, dispatch.getStatus());
        }
        if (StringUtils.isNotEmpty(dispatch.getOrderId())) {
            lambdaQueryWrapper.like(TaskPickupDispatch::getOrderId, dispatch.getOrderId());
        }
        lambdaQueryWrapper.orderBy(true, false, TaskPickupDispatch::getId);
        return list(lambdaQueryWrapper);
    }
}
