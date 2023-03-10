package com.itheima.pinda.service.truck;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;

import java.util.List;

/**
 * 车辆类型与货物类型关联
 */
public interface IPdTruckTypeGoodsTypeService extends IService<PdTruckTypeGoodsType> {

    /**
     * 批量添加车辆类型与货物类型关联
     * @param pdTruckTypeGoodsTypes
     */
    public void batchSave(List<PdTruckTypeGoodsType> pdTruckTypeGoodsTypes);

    /**
     * 根据车辆类型或者货物类型查询关联信息
     * @param truckTypeId
     * @param goodsTypeId
     * @return
     */
    public List<PdTruckTypeGoodsType> findAll(String truckTypeId, String goodsTypeId);


    /**
     * 删除关联关系
     *
     * @param truckTypeId 车辆类型id
     * @param goodsTypeId 货物类型id
     */
    void delete(String truckTypeId, String goodsTypeId);
}
