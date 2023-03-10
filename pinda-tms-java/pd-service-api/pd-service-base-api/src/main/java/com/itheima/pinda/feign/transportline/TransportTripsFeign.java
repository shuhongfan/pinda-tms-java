package com.itheima.pinda.feign.transportline;

import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.transportline.TransportTripsDto;
import com.itheima.pinda.DTO.transportline.TransportTripsTruckDriverDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("base/transportLine/trips")
@ApiIgnore
public interface TransportTripsFeign {
    /**
     * 添加车次
     *
     * @param dto 车次信息
     * @return 车次信息
     */
    @PostMapping("")
    TransportTripsDto save(@RequestBody TransportTripsDto dto);

    /**
     * 根据id获取车次详情
     *
     * @param id 车次id
     * @return 车次信息
     */
    @GetMapping("/{id}")
    TransportTripsDto fineById(@PathVariable(name = "id") String id);

    /**
     * 获取车次列表
     *
     * @param transportLineId 线路id
     * @param ids             车次id列表
     * @return 车次列表
     */
    @GetMapping("")
    List<TransportTripsDto> findAll(@RequestParam(name = "transportLineId", required = false) String transportLineId, @RequestParam(name = "ids", required = false) List<String> ids);

    /**
     * 更新车次信息
     *
     * @param id  车次id
     * @param dto 车次信息
     * @return 车次信息
     */
    @PutMapping("/{id}")
    TransportTripsDto update(@PathVariable(name = "id") String id, @RequestBody TransportTripsDto dto);

    /**
     * 删除车次信息
     *
     * @param id 车次信息
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    Result disable(@PathVariable(name = "id") String id);

    /**
     * 批量保存车次与车辆和司机关联关系
     *
     * @param dtoList 车次与车辆和司机关联关系
     * @return 返回信息
     */
    @PostMapping("{id}/truckDriver")
    Result batchSaveTruckDriver(@PathVariable("id") String transportTripsId, @RequestBody List<TransportTripsTruckDriverDto> dtoList);

    /**
     * 获取车次与车辆和司机关联关系列表
     *
     * @param transportTripsId 车次id
     * @param truckId          车辆id
     * @param userId           司机id
     * @return 车次与车辆和司机关联关系列表
     */
    @GetMapping("/truckDriver")
    List<TransportTripsTruckDriverDto> findAllTruckDriverTransportTrips(@RequestParam(name = "transportTripsId", required = false) String transportTripsId, @RequestParam(name = "truckId", required = false) String truckId, @RequestParam(name = "userId", required = false) String userId);
}
