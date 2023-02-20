package com.itheima.pinda.mapper.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.pinda.entity.base.PdGoodsType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 货物类型Mapper接口
 */
@Mapper
public interface PdGoodsTypeMapper extends BaseMapper<PdGoodsType>{
    List<PdGoodsType> findByPage(Page<PdGoodsType> page,
                                 @Param("name")String name,
                                 @Param("truckTypeId")String truckTypeId,
                                 @Param("truckTypeName")String truckTypeName);
}
