package com.itheima.pinda.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.DTO.OrderSearchDTO;
import com.itheima.pinda.entity.Order;

import java.util.List;

/**
 * <p>
 * 订单状态  服务类
 * </p>
 *
 * @author jpf
 * @since 2019-12-26
 */
public interface IOrderService extends IService<Order> {

    /**
     * 新增订单
     *
     * @param order 订单信息
     * @return 订单信息
     */
    Order saveOrder(Order order);

    /**
     * 获取订单分页数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param order    查询条件
     * @return 订单分页数据
     */
    IPage<Order> findByPage(Integer page, Integer pageSize, Order order);

    /**
     * 获取订单列表
     *
     * @param ids 订单id列表
     * @return 订单列表
     */
    List<Order> findAll(List<String> ids);

    /**
     * 获取订单分页数据 客户端使用
     *
     * @return 订单分页数据
     */
    IPage<Order> pageLikeForCustomer(OrderSearchDTO orderSearchDTO);
}
