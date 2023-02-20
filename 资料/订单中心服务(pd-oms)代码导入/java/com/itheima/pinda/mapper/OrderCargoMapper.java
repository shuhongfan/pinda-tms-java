package com.itheima.pinda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pinda.entity.OrderCargo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 货品总重量  Mapper 接口
 * </p>
 *
 * @author jpf
 * @since 2019-12-26
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
