package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.DTO.truck.TruckTypeDto;
import com.itheima.pinda.common.utils.Constant;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.base.PdGoodsType;
import com.itheima.pinda.entity.truck.PdTruckTypeGoodsType;
import com.itheima.pinda.service.base.IPdGoodsTypeService;
import com.itheima.pinda.service.truck.IPdTruckTypeGoodsTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public GoodsTypeDto saveGoodsType(@Validated @RequestBody GoodsTypeDto goodsTypeDto) {
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

    /**
     * 根据id查询货物类型
     *
     * @param id 货物类型id
     * @return 货物类型信息
     */
    @GetMapping("/{id}")
    public GoodsTypeDto findById(@PathVariable("id") String id) {
//        查询基本信息
        PdGoodsType pdGoodsType = goodsTypeService.getById(id);

        GoodsTypeDto goodsTypeDto = new GoodsTypeDto();
        BeanUtils.copyProperties(pdGoodsType, goodsTypeDto);

//        还需要查询当前货物类型关联的车辆类型id
        List<PdTruckTypeGoodsType> pdTruckTypeGoodsTypeList = truckTypeGoodsTypeService.findAll(null, id);
        if (pdTruckTypeGoodsTypeList != null && pdTruckTypeGoodsTypeList.size() > 0) {
            List<String> truckTypeIdList = pdTruckTypeGoodsTypeList.stream().map(PdTruckTypeGoodsType::getTruckTypeId).collect(Collectors.toList());

            goodsTypeDto.setTruckTypeIds(truckTypeIdList);
        }

        return goodsTypeDto;
    }

    /**
     * 查询所有货物类型
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "查询所有货物类型")
    public List<GoodsTypeDto> findAll() {
        List<PdGoodsType> goodsTypeList = goodsTypeService.findAll();
        if (goodsTypeList != null && goodsTypeList.size() > 0) {
            return goodsTypeList.stream().map(goodsType -> {
                GoodsTypeDto goodsTypeDto = new GoodsTypeDto();
                BeanUtils.copyProperties(goodsType, goodsTypeList);
                return goodsTypeDto;
            }).collect(Collectors.toList());
        }
        return null;
    }

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
    @GetMapping("/page")
    @ApiOperation(value = "获取分页货物类型数据")
    public PageResponse<GoodsTypeDto> findByPage(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "pageSize") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "truckTypeId", required = false) String truckTypeId,
            @RequestParam(name = "truckTypeName", required = false) String truckTypeName) {
        IPage<PdGoodsType> goodsTypeIPage = goodsTypeService.findByPage(page, pageSize, name, truckTypeId, truckTypeName);
        List<PdGoodsType> goodsTypeIPageRecords = goodsTypeIPage.getRecords();

        if (goodsTypeIPageRecords != null && goodsTypeIPageRecords.size() > 0) {
            List<GoodsTypeDto> goodsTypeDtoList = goodsTypeIPageRecords.stream().map(goodsType -> {
                List<PdTruckTypeGoodsType> truckTypeGoodsTypeList = truckTypeGoodsTypeService.findAll(null, goodsType.getId());
                List<String> truckTypeIds = truckTypeGoodsTypeList.stream().map(PdTruckTypeGoodsType::getTruckTypeId).collect(Collectors.toList());

                GoodsTypeDto goodsTypeDto = new GoodsTypeDto();
                BeanUtils.copyProperties(goodsType, goodsTypeDto);
                goodsTypeDto.setTruckTypeIds(truckTypeIds);
                return goodsTypeDto;
            }).collect(Collectors.toList());

            return PageResponse.<GoodsTypeDto>builder()
                    .items(goodsTypeDtoList)
                    .counts(goodsTypeIPage.getSize())
                    .page(page)
                    .pages(goodsTypeIPage.getPages())
                    .pagesize(pageSize)
                    .build();
        }

        return null;
    }

    /**
     * 获取货物类型列表
     *
     * @return 货物类型列表
     */
    @GetMapping("")
    @ApiOperation(value = "获取货物类型列表")
    public List<GoodsTypeDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids) {
        List<PdGoodsType> pdGoodsTypeList = goodsTypeService.findAll(ids);
        if (pdGoodsTypeList != null && pdGoodsTypeList.size() > 0) {
            return pdGoodsTypeList.stream().map(pdGoodsType -> {
                List<PdTruckTypeGoodsType> typeGoodsTypeList = truckTypeGoodsTypeService.findAll(null, pdGoodsType.getId());
                List<String> truckTypeIds = typeGoodsTypeList.stream().map(PdTruckTypeGoodsType::getTruckTypeId).collect(Collectors.toList());

                GoodsTypeDto goodsTypeDto = new GoodsTypeDto();
                BeanUtils.copyProperties(pdGoodsTypeList, goodsTypeDto);
                goodsTypeDto.setTruckTypeIds(truckTypeIds);
                return goodsTypeDto;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 更新货物类型信息
     *
     * @param dto 货物类型信息
     * @return 货物类型信息
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新货物类型信息")
    public GoodsTypeDto update(@PathVariable(name = "id") String id, @RequestBody GoodsTypeDto dto) {
        PdGoodsType pdGoodsType = new PdGoodsType();
        BeanUtils.copyProperties(dto, pdGoodsType);
        pdGoodsType.setId(id);

//        更新货物类型的普通属性
        goodsTypeService.updateById(pdGoodsType);

//        跟新关联信息
        List<String> truckTypeIds = dto.getTruckTypeIds();
        if (truckTypeIds != null && truckTypeIds.size() > 0) {
//            清理原有关联信息
            truckTypeGoodsTypeService.delete(null, id);

//            重新建立关联关系
            List<PdTruckTypeGoodsType> goodsTypeList = truckTypeIds.stream().map(truckTypeId -> {
                PdTruckTypeGoodsType pdTruckTypeGoodsType = new PdTruckTypeGoodsType();
                pdTruckTypeGoodsType.setGoodsTypeId(id);
                pdTruckTypeGoodsType.setTruckTypeId(truckTypeId);
                return pdTruckTypeGoodsType;
            }).collect(Collectors.toList());

//            批量保存货物类型和车辆类型的关联信息
            truckTypeGoodsTypeService.batchSave(goodsTypeList);
        }

        dto.setId(id);
        return dto;
    }

    /**
     * 删除货物类型
     *
     * @param id 货物类型id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    @ApiOperation(value = "删除货物类型")
    public Result disable(@PathVariable(name = "id") String id) {
        PdGoodsType pdGoodsType = new PdGoodsType();
        pdGoodsType.setId(id);
        pdGoodsType.setStatus(Constant.DATA_DISABLE_STATUS);
        goodsTypeService.updateById(pdGoodsType);
        return Result.ok();
    }
}
