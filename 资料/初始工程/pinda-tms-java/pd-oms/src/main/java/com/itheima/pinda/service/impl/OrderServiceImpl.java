package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.DTO.OrderDTO;
import com.itheima.pinda.DTO.OrderSearchDTO;
import com.itheima.pinda.common.utils.CustomIdGenerator;
import com.itheima.pinda.entity.Order;
import com.itheima.pinda.enums.OrderPaymentStatus;
import com.itheima.pinda.enums.OrderPickupType;
import com.itheima.pinda.enums.OrderStatus;
import com.itheima.pinda.mapper.OrderMapper;
import com.itheima.pinda.service.IOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private CustomIdGenerator idGenerator;

    @Override
    public Order saveOrder(Order order) {
        order.setId(idGenerator.nextId(order) + "");
        order.setCreateTime(LocalDateTime.now());
        order.setPaymentStatus(OrderPaymentStatus.UNPAID.getStatus());
        if (OrderPickupType.NO_PICKUP.getCode() == order.getPickupType()) {
            order.setStatus(OrderStatus.OUTLETS_SINCE_SENT.getCode());
        } else {
            order.setStatus(OrderStatus.PENDING.getCode());
        }
        save(order);
        return order;
    }

    @Override
    public IPage<Order> findByPage(Integer page, Integer pageSize, Order order) {
        Page<Order> iPage = new Page(page, pageSize);
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(order.getId())) {
            lambdaQueryWrapper.like(Order::getId, order.getId());
        }
        if (order.getStatus() != null) {
            lambdaQueryWrapper.eq(Order::getStatus, order.getStatus());
        }
        if (order.getPaymentStatus() != null) {
            lambdaQueryWrapper.eq(Order::getPaymentStatus, order.getPaymentStatus());
        }
        //发件人信息
        if (StringUtils.isNotEmpty(order.getSenderName())) {
            lambdaQueryWrapper.like(Order::getSenderName, order.getSenderName());
        }
        if (StringUtils.isNotEmpty(order.getSenderPhone())) {
            lambdaQueryWrapper.like(Order::getSenderPhone, order.getSenderPhone());
        }
        if (StringUtils.isNotEmpty(order.getSenderProvinceId())) {
            lambdaQueryWrapper.eq(Order::getSenderProvinceId, order.getSenderProvinceId());
        }
        if (StringUtils.isNotEmpty(order.getSenderCityId())) {
            lambdaQueryWrapper.eq(Order::getSenderCityId, order.getSenderCityId());
        }
        if (StringUtils.isNotEmpty(order.getSenderCountyId())) {
            lambdaQueryWrapper.eq(Order::getSenderCountyId, order.getSenderCountyId());
        }
        //收件人信息
        if (StringUtils.isNotEmpty(order.getReceiverName())) {
            lambdaQueryWrapper.like(Order::getReceiverName, order.getReceiverName());
        }
        if (StringUtils.isNotEmpty(order.getReceiverPhone())) {
            lambdaQueryWrapper.like(Order::getReceiverPhone, order.getReceiverPhone());
        }
        if (StringUtils.isNotEmpty(order.getReceiverProvinceId())) {
            lambdaQueryWrapper.eq(Order::getReceiverProvinceId, order.getReceiverProvinceId());
        }
        if (StringUtils.isNotEmpty(order.getReceiverCityId())) {
            lambdaQueryWrapper.eq(Order::getReceiverCityId, order.getReceiverCityId());
        }
        if (StringUtils.isNotEmpty(order.getReceiverCountyId())) {
            lambdaQueryWrapper.eq(Order::getReceiverCountyId, order.getReceiverCountyId());
        }
        lambdaQueryWrapper.orderBy(true, false, Order::getId);
        return page(iPage, lambdaQueryWrapper);
    }

    @Override
    public List<Order> findAll(List<String> ids) {
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (ids != null && ids.size() > 0) {
            lambdaQueryWrapper.in(Order::getId, ids);
        }
        lambdaQueryWrapper.orderBy(true, false, Order::getId);
        return list(lambdaQueryWrapper);
    }

    @Override
    public IPage<Order> pageLikeForCustomer(OrderSearchDTO orderSearchDTO) {

        Integer page = orderSearchDTO.getPage();
        Integer pageSize = orderSearchDTO.getPageSize();

        IPage<Order> ipage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Order> orderQueryWrapper = new LambdaQueryWrapper<>();
        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getId()), Order::getId, orderSearchDTO.getId());
        orderQueryWrapper.like(StringUtils.isNotEmpty(orderSearchDTO.getKeyword()), Order::getId, orderSearchDTO.getKeyword());
        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getMemberId()), Order::getMemberId, orderSearchDTO.getMemberId());
        orderQueryWrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getReceiverPhone()), Order::getReceiverPhone, orderSearchDTO.getReceiverPhone());
        orderQueryWrapper.orderByDesc(Order::getCreateTime);
        return page(ipage, orderQueryWrapper);
    }

    //@Autowired
    //private KieContainer kieContainer;

    /**
     * 计算订单价格
     * @param orderDTO
     * @return
     */
    public Map calculateAmount(OrderDTO orderDTO) {
        return null;
    }

    /**
     * 调用百度地图服务接口，根据寄件人地址和收件人地址计算订单距离
     * @param orderDTO
     * @return
     */
    public OrderDTO getDistance(OrderDTO orderDTO){
        //调用百度地图服务接口获取寄件人地址对应的坐标经纬度
        return null;
    }
}
