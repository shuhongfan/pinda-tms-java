package com.itheima.pinda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pinda.entity.TransportOrder;
import com.itheima.pinda.entity.TransportOrderTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 运单与运输任务表 Mapper 接口
 * </p>
 */
@Mapper
public interface TransportOrderTaskMapper extends BaseMapper<TransportOrderTask> {
}
