package com.itheima.pinda.feign.truck;

import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.truck.TruckTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.List;


@FeignClient(name = "pd-base")
@RequestMapping("base/truck/type")
@ApiIgnore
public interface TruckTypeFeign {
    /**
     * 添加车辆类型
     *
     * @param dto 车辆类型信息
     * @return 车辆类型信息
     */
    @PostMapping("")
    TruckTypeDto saveTruckType(@RequestBody TruckTypeDto dto);

    /**
     * 根据id获取车辆类型详情
     *
     * @param id 车辆类型id
     * @return 车辆类型信息
     */
    @GetMapping("/{id}")
    TruckTypeDto fineById(@PathVariable(name = "id") String id);

    /**
     * 获取车辆类型分页数据
     *
     * @param page            页码
     * @param pageSize        页尺寸
     * @param name            车辆类型名称
     * @param allowableLoad   车辆载重
     * @param allowableVolume 车辆体积
     * @return 车辆类型分页数据
     */
    @GetMapping("/page")
    PageResponse<TruckTypeDto> findByPage(@RequestParam(name = "page") Integer page,
                                          @RequestParam(name = "pageSize") Integer pageSize,
                                          @RequestParam(name = "name", required = false) String name,
                                          @RequestParam(name = "allowableLoad", required = false) BigDecimal allowableLoad,
                                          @RequestParam(name = "allowableVolume", required = false) BigDecimal allowableVolume);

    /**
     * 获取车辆类型列表
     *
     * @param ids 车辆类型id
     * @return 车辆类型列表
     */
    @GetMapping("")
    List<TruckTypeDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids);

    /**
     * 更新车辆类型信息
     *
     * @param id  车辆类型id
     * @param dto 车辆类型信息
     * @return 车辆类型信息
     */
    @PutMapping("/{id}")
    TruckTypeDto update(@PathVariable(name = "id") String id, @RequestBody TruckTypeDto dto);

    /**
     * 删除车辆类型
     *
     * @param id 车辆类型Id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    Result disable(@PathVariable(name = "id") String id);
}
