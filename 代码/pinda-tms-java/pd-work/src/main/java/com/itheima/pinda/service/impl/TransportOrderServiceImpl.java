package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.common.CustomIdGenerator;
import com.itheima.pinda.entity.TransportOrder;
import com.itheima.pinda.enums.transportorder.TransportOrderSchedulingStatus;
import com.itheima.pinda.enums.transportorder.TransportOrderStatus;
import com.itheima.pinda.mapper.TransportOrderMapper;
import com.itheima.pinda.service.ITransportOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 运单服务实现类
 * </p>
 */
@Service
public class TransportOrderServiceImpl extends
        ServiceImpl<TransportOrderMapper, TransportOrder> implements ITransportOrderService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public TransportOrder saveTransportOrder(TransportOrder transportOrder) {
        transportOrder.setCreateTime(LocalDateTime.now());
        transportOrder.setId(idGenerator.nextId(transportOrder) + "");
        transportOrder.setStatus(TransportOrderStatus.CREATED.getCode());
        transportOrder.setSchedulingStatus(TransportOrderSchedulingStatus.TO_BE_SCHEDULED.getCode());
        save(transportOrder);
        return transportOrder;
    }

    @Override
    public IPage<TransportOrder> findByPage(Integer page, Integer pageSize, String orderId, Integer status, Integer schedulingStatus) {
        Page<TransportOrder> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<TransportOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(orderId)) {
            lambdaQueryWrapper.like(TransportOrder::getOrderId, orderId);
        }
        if (status != null) {
            lambdaQueryWrapper.eq(TransportOrder::getStatus, status);
        }
        if (schedulingStatus != null) {
            lambdaQueryWrapper.eq(TransportOrder::getSchedulingStatus, schedulingStatus);
        }
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<TransportOrder> findAll(List<String> ids, String orderId, Integer status, Integer schedulingStatus) {
        LambdaQueryWrapper<TransportOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(TransportOrder::getId, ids);
        }
        if (StringUtils.isNotEmpty(orderId)) {
            lambdaQueryWrapper.like(TransportOrder::getOrderId, orderId);
        }
        if (status != null) {
            lambdaQueryWrapper.eq(TransportOrder::getStatus, status);
        }
        if (schedulingStatus != null) {
            lambdaQueryWrapper.eq(TransportOrder::getSchedulingStatus, schedulingStatus);
        }
        return list(lambdaQueryWrapper);
    }

    @Override
    public TransportOrder findByOrderId(String orderId) {
        return getOne(new LambdaQueryWrapper<TransportOrder>().eq(TransportOrder::getOrderId, orderId));
    }
}
