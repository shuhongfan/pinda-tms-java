package com.itheima.pinda.mapper.truck;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆类型与货物类型关联Mapper接口
 */
@Mapper
public interface PdTruckTypeGoodsTypeMapper extends BaseMapper<PdTruckTypeGoodsType> {
}
