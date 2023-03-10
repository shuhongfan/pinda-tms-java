package com.itheima.pinda.controller;

import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.DTO.truck.TruckTypeDto;
import com.itheima.pinda.entity.base.PdGoodsType;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;
import com.itheima.pinda.service.base.IPdGoodsTypeService;
import com.itheima.pinda.service.truck.IPdTruckTypeGoodsTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 货物类型管理
 */
@RestController
@RequestMapping("base/goodsType")
@Api(tags = "货物类型管理")
public class GoodsTypeController {
    @Autowired
    private IPdGoodsTypeService goodsTypeService;

    @Autowired
    private IPdTruckTypeGoodsTypeService truckTypeGoodsTypeService;

    /**
     * 新增货物类型，同时需要关联车辆类型
     *
     * @param goodsTypeDto 货物类型信息
     * @return 货物类型信息
     */
    @PostMapping("")
    @ApiOperation(value = "添加货物类型")
    public GoodsTypeDto saveGoodsType(@RequestBody GoodsTypeDto goodsTypeDto) {
        PdGoodsType pdGoodsType = new PdGoodsType();
        BeanUtils.copyProperties(goodsTypeDto, pdGoodsType);

//        保存货物类型信息到货物类型表
        pdGoodsType = goodsTypeService.saveGoodsType(pdGoodsType);
        String goodsTypeId = pdGoodsType.getId();

//        保存货物类型和车辆类型关联信息到关联表
        List<String> truckTypeIds = goodsTypeDto.getTruckTypeIds();
        if (truckTypeIds != null && truckTypeIds.size() > 0) {
            List<PdTruckTypeGoodsType> list = truckTypeIds.stream().map(truckTypeId -> {
                PdTruckTypeGoodsType pdTruckTypeGoodsType = new PdTruckTypeGoodsType();
                pdTruckTypeGoodsType.setGoodsTypeId(goodsTypeId);  // 货物类型id
                pdTruckTypeGoodsType.setTruckTypeId(truckTypeId); // 车辆id
                return pdTruckTypeGoodsType;
            }).collect(Collectors.toList());
//            批量保存货物类型和车辆类型的关联信息
            truckTypeGoodsTypeService.batchSave(list);
        }

        BeanUtils.copyProperties(pdGoodsType, goodsTypeDto);
        return goodsTypeDto;
    }
}
