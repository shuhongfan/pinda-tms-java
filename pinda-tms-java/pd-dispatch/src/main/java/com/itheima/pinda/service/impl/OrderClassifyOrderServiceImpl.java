package com.itheima.pinda.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.entity.OrderClassifyOrderEntity;
import com.itheima.pinda.mapper.OrderClassifyOrderMapper;
import com.itheima.pinda.service.IOrderClassifyOrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderClassifyOrderServiceImpl extends ServiceImpl<OrderClassifyOrderMapper, OrderClassifyOrderEntity> implements IOrderClassifyOrderService {
    @Override
    public String getClassifyId(String orderId) {
        LambdaQueryWrapper<OrderClassifyOrderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderClassifyOrderEntity::getOrderId, orderId);

        OrderClassifyOrderEntity entity = this.baseMapper.selectOne(wrapper);

        return entity.getOrderClassifyId();
    }
}
