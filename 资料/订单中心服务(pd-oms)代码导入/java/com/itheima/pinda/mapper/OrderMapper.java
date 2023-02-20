package com.itheima.pinda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pinda.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 订单状态  Mapper 接口
 * </p>
 *
 * @author jpf
 * @since 2019-12-26
 */
@Component
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
