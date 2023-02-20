package com.itheima.pinda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pinda.entity.OrderCargo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 货物Mapper 接口
 */
@Mapper
public interface OrderCargoMapper extends BaseMapper<OrderCargo> {

  int deleteByPrimaryKey(String id);

  int insertSelective(OrderCargo record);

  OrderCargo selectByPrimaryKey(String id);

  int updateByPrimaryKeySelective(OrderCargo record);

  int updateByPrimaryKeyWithBLOBs(OrderCargo record);

  int updateByPrimaryKey(OrderCargo record);
}
