package com.itheima.pinda.feign.user;

import com.itheima.pinda.DTO.user.TruckDriverDto;
import com.itheima.pinda.DTO.user.TruckDriverLicenseDto;
import com.itheima.pinda.common.utils.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("sys/driver")
@ApiIgnore
public interface DriverFeign {
    /**
     * 保存司机基本信息
     *
     * @param dto 司机基本信息
     * @return 返回信息
     */
    @PostMapping
    TruckDriverDto saveDriver(@RequestBody TruckDriverDto dto);

    /**
     * 获取司机基本信息列表
     *
     * @param userIds 司机id列表
     * @param fleetId 车队id
     * @return 司机基本信息列表
     */
    @GetMapping
    List<TruckDriverDto> findAllDriver(@RequestParam(name = "userIds",required = false) List<String> userIds, @RequestParam(name = "fleetId",required = false) String fleetId);

    /**
     * 获取司机基本信息
     *
     * @param id 司机id
     * @return 司机基本信息
     */
    @GetMapping("/{id}")
    TruckDriverDto findOneDriver(@PathVariable(name = "id") String id);

    /**
     * 保存司机驾驶证信息
     *
     * @param dto 司机驾驶证信息
     * @return 返回信息
     */
    @PostMapping("/driverLicense")
    TruckDriverLicenseDto saveDriverLicense(@RequestBody TruckDriverLicenseDto dto);

    /**
     * 获取司机驾驶证信息
     *
     * @param id 司机id
     * @return 司机驾驶证信息
     */
    @GetMapping("/{id}/driverLicense")
    TruckDriverLicenseDto findOneDriverLicense(@PathVariable(name = "id") String id);

    /**
     * 统计司机数量
     *
     * @param fleetId 车队id
     * @return 司机数量
     */
    @GetMapping("/count")
    Integer count(@RequestParam(name = "fleetId", required = false) String fleetId);

    /**
     * 获取司机分页数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @param fleetId  车队id
     * @return 司机分页数据
     */
    @GetMapping("/page")
    PageResponse<TruckDriverDto> findByPage(@RequestParam(name = "page") Integer page,
                                            @RequestParam(name = "pageSize") Integer pageSize,
                                            @RequestParam(name = "fleetId", required = false) String fleetId);


    /**
     * 查询全部
     * @param ids
     * @return
     */
    @GetMapping("/findAll")
    List<TruckDriverDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids);
}
