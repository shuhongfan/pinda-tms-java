package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.TransportOrder;
import com.itheima.pinda.entity.TransportOrderTask;
import com.itheima.pinda.enums.transportorder.TransportOrderSchedulingStatus;
import com.itheima.pinda.enums.transportorder.TransportOrderStatus;
import com.itheima.pinda.mapper.TransportOrderTaskMapper;
import com.itheima.pinda.service.ITransportOrderTaskService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 运单、运输任务关系服务实现类
 * </p>
 */
@Service
public class TransportOrderTaskServiceImpl extends
        ServiceImpl<TransportOrderTaskMapper, TransportOrderTask> implements ITransportOrderTaskService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public void batchSaveTransportOrder(List<TransportOrderTask> transportOrderTaskList) {
        transportOrderTaskList.forEach(transportOrderTask -> transportOrderTask.setId(idGenerator.nextId(transportOrderTask) + ""));
        saveBatch(transportOrderTaskList);
    }

    @Override
    public IPage<TransportOrderTask> findByPage(Integer page, Integer pageSize, String transportOrderId, String transportTaskId) {
        Page<TransportOrderTask> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<TransportOrderTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(transportOrderId)) {
            lambdaQueryWrapper.like(TransportOrderTask::getTransportOrderId, transportOrderId);
        }
        if (StringUtils.isNotEmpty(transportTaskId)) {
            lambdaQueryWrapper.like(TransportOrderTask::getTransportTaskId, transportTaskId);
        }
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<TransportOrderTask> findAll(String transportOrderId, String transportTaskId) {
        LambdaQueryWrapper<TransportOrderTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(transportOrderId)) {
            lambdaQueryWrapper.like(TransportOrderTask::getTransportOrderId, transportOrderId);
        }
        if (StringUtils.isNotEmpty(transportTaskId)) {
            lambdaQueryWrapper.like(TransportOrderTask::getTransportTaskId, transportTaskId);
        }
        lambdaQueryWrapper.orderBy(true, true, TransportOrderTask::getId);
        return list(lambdaQueryWrapper);
    }

    @Override
    public Integer count(String transportOrderId, String transportTaskId) {
        LambdaQueryWrapper<TransportOrderTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(transportOrderId)) {
            lambdaQueryWrapper.like(TransportOrderTask::getTransportOrderId, transportOrderId);
        }
        if (StringUtils.isNotEmpty(transportTaskId)) {
            lambdaQueryWrapper.like(TransportOrderTask::getTransportTaskId, transportTaskId);
        }
        return count(lambdaQueryWrapper);
    }

    @Override
    public void del(String transportOrderId, String transportTaskId) {
        boolean isDel = false;
        LambdaQueryWrapper<TransportOrderTask> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(transportOrderId)) {
            lambdaQueryWrapper.eq(TransportOrderTask::getTransportOrderId, transportOrderId);
            isDel = true;
        }
        if (StringUtils.isNotEmpty(transportTaskId)) {
            lambdaQueryWrapper.eq(TransportOrderTask::getTransportTaskId, transportTaskId);
            isDel = true;
        }
        if (isDel) {
            remove(lambdaQueryWrapper);
        }
    }
}
