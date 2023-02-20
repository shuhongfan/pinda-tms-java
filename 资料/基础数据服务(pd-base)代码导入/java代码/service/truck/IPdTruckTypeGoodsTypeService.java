package com.itheima.pinda.service.truck;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;

import java.util.List;

/**
 * <p>
 * 车辆类型与货物类型关联表 服务类
 * </p>
 *
 * @author itcast
 * @since 2019-12-20
 */
public interface IPdTruckTypeGoodsTypeService extends IService<PdTruckTypeGoodsType> {
    /**
     * 添加车辆类型与货物类型关联
     *
     * @param pdTruckTypeGoodsType 车辆类型与货物类型信息
     */
    void saveTruckTypeGoodsType(PdTruckTypeGoodsType pdTruckTypeGoodsType);

    /**
     * 批量添加车辆类型与货物类型关联
     *
     * @param truckTypeGoodsTypeList 车辆类型与货物类型信息
     */
    void batchSave(List<PdTruckTypeGoodsType> truckTypeGoodsTypeList);

    /**
     * 删除关联关系
     *
     * @param truckTypeId 车辆类型id
     * @param goodsTypeId 货物类型id
     */
    void delete(String truckTypeId, String goodsTypeId);

    /**
     * 获取车辆类型与货物类型关联
     *
     * @param truckTypeId 车辆类型id
     * @param goodsTypeId 货物类型id
     * @return 车辆类型与货物类型关联
     */
    List<PdTruckTypeGoodsType> findAll(String truckTypeId, String goodsTypeId);
}
