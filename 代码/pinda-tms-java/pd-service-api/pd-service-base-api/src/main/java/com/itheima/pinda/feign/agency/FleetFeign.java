package com.itheima.pinda.feign.agency;

import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.angency.FleetDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("sys/agency/fleet")
@ApiIgnore
public interface FleetFeign {
    /**
     * 添加车队
     *
     * @param dto 车队信息
     * @return 车队信息
     */
    @PostMapping("")
    FleetDto saveAgencyType(@RequestBody FleetDto dto);

    /**
     * 根据id获取车队详情
     *
     * @param id 车队id
     * @return 车队信息
     */
    @GetMapping("/{id}")
    FleetDto fineById(@PathVariable(name = "id") String id);

    /**
     * 获取车队分页数据
     *
     * @param page        页码
     * @param pageSize    页尺寸
     * @param name        车队名称
     * @param fleetNumber 车队编号
     * @param manager     负责人id
     * @return 车队分页数据
     */
    @GetMapping("/page")
    PageResponse<FleetDto> findByPage(@RequestParam(name = "page") Integer page,
                                      @RequestParam(name = "pageSize") Integer pageSize,
                                      @RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "fleetNumber", required = false) String fleetNumber,
                                      @RequestParam(name = "manager", required = false) String manager);

    /**
     * 获取车队列表
     *
     * @param ids 车队Id列表
     * @return 车队列表
     */
    @GetMapping("")
    List<FleetDto> findAll(@RequestParam(value = "ids",required = false) List<String> ids,@RequestParam(value = "agencyId",required = false) String agencyId);

    /**
     * 更新车队信息
     *
     * @param dto 车队信息
     * @return 车队信息
     */
    @PutMapping("/{id}")
    FleetDto update(@PathVariable(name = "id") String id, @RequestBody FleetDto dto);

    /**
     * 删除车队
     *
     * @param id 车队id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    Result disable(@PathVariable(name = "id") String id);
}
