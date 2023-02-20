package com.itheima.pinda.feign.truck;

import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.truck.TruckDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("base/truck")
@ApiIgnore
public interface TruckFeign {
    /**
     * 添加车辆
     *
     * @param dto 车辆信息
     * @return 车辆信息
     */
    @PostMapping("")
    TruckDto saveTruck(@RequestBody TruckDto dto);

    /**
     * 根据id获取车辆详情
     *
     * @param id 车辆id
     * @return 车辆信息
     */
    @GetMapping("/{id}")
    TruckDto fineById(@PathVariable(name = "id") String id);

    /**
     * 获取车辆分页数据
     *
     * @param page         页码
     * @param pageSize     页尺寸
     * @param truckTypeId  车辆类型id
     * @param licensePlate 车牌好吗
     * @return 车辆分页数据
     */
    @GetMapping("/page")
    PageResponse<TruckDto> findByPage(@RequestParam(name = "page") Integer page,
                                      @RequestParam(name = "pageSize") Integer pageSize,
                                      @RequestParam(name = "truckTypeId", required = false) String truckTypeId,
                                      @RequestParam(name = "licensePlate", required = false) String licensePlate,
                                      @RequestParam(name = "fleetId", required = false) String fleetId);

    /**
     * 获取车辆列表
     *
     * @param ids     车辆id列表
     * @param fleetId 车队id
     * @return 车辆列表
     */
    @GetMapping("")
    List<TruckDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids, @RequestParam(name = "fleetId", required = false) String fleetId);

    /**
     * 更新车辆信息
     *
     * @param id  车辆id
     * @param dto 车辆信息
     * @return 车辆信息
     */
    @PutMapping("/{id}")
    TruckDto update(@PathVariable(name = "id") String id, @RequestBody TruckDto dto);

    /**
     * 统计车辆数量
     *
     * @param fleetId 车队id
     * @return 车辆数量
     */
    @GetMapping("/count")
    Integer count(@RequestParam(name = "fleetId", required = false) String fleetId);

    /**
     * 删除车辆
     *
     * @param id 车辆id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    Result disable(@PathVariable(name = "id") String id);
}
