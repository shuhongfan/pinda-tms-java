package com.itheima.pinda.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.OrderClassifyOrderEntity;

/**
 * 订单分类关联订单
 */
public interface IOrderClassifyOrderService extends IService<OrderClassifyOrderEntity> {
    String getClassifyId(String orderId);
}