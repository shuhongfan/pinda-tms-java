package com.itheima.pinda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.pinda.entity.TransportOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 运单表 Mapper 接口
 * </p>
 *
 * @author jpf
 * @since 2020-01-06
 */
@Mapper
public interface TransportOrderMapper extends BaseMapper<TransportOrder> {
}
