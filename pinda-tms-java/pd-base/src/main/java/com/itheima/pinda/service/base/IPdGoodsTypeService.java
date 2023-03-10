package com.itheima.pinda.service.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.base.PdGoodsType;

import java.util.List;

/**
 * 车辆类型和货物类型中间关系表
 */
public interface IPdGoodsTypeService extends IService<PdGoodsType> {

    /**
     * 添加货物类型
     *
     * @param pdGoodsType 货物类型信息
     * @return 货物类型信息
     */
    PdGoodsType saveGoodsType(PdGoodsType pdGoodsType);

    /**
     * 查询所有货物类型
     * @return
     */
    List<PdGoodsType> findAll();

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
    public IPage<PdGoodsType> findByPage(Integer page, Integer pageSize, String name, String truckTypeId, String truckTypeName);

    /**
     * 查询所有货物类型
     * @return
     */
    List<PdGoodsType> findAll(List<String> ids);

}
