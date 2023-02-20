package com.itheima.pinda.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.pinda.entity.OrderLocation;
import com.itheima.pinda.mapper.OrderLocationMapper;
import com.itheima.pinda.service.IOrderLocationService;
import org.springframework.stereotype.Service;

/**
 * 位置信息服务实现
 */
@Service
public class OrderLocationServiceImpl extends ServiceImpl<OrderLocationMapper, OrderLocation> implements IOrderLocationService {

}