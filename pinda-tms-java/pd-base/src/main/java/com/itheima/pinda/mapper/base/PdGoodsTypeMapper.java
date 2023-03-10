package com.itheima.pinda.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.pinda.entity.base.PdGoodsType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物类型Mapper接口
 */
@Mapper
public interface PdGoodsTypeMapper extends BaseMapper<PdGoodsType> {
    /**
     * 获取分页货物类型数据
     *
     * @param page          页码
     * @param pageSize      页尺寸
     * @param name          货物类型名称
     * @param truckTypeId   车辆类型Id
     * @param truckTypeName 车辆类型名称
     * @return
     */
    List<PdGoodsType> findByPage(Page<PdGoodsType> page,
                                 @Param("name")String name,
                                 @Param("truckTypeId")String truckTypeId,
                                 @Param("truckTypeName")String truckTypeName);
}
