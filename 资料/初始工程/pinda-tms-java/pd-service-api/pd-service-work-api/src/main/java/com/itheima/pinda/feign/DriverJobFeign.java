package com.itheima.pinda.feign;

import com.itheima.pinda.DTO.DriverJobDTO;
import com.itheima.pinda.common.utils.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("pd-work")
@RequestMapping("driver-job")
public interface DriverJobFeign {
    /**
     * 新增司机作业单
     *
     * @param dto 司机作业单信息
     * @return 司机作业单信息
     */
    @PostMapping("")
    DriverJobDTO save(@RequestBody DriverJobDTO dto);

    /**
     * 修改司机作业单信息
     *
     * @param id  司机作业单id
     * @param dto 司机作业单信息
     * @return 司机作业单信息
     */
    @PutMapping("/{id}")
    DriverJobDTO updateById(@PathVariable(name = "id") String id, @RequestBody DriverJobDTO dto);

    /**
     * 获取司机作业单分页数据
     *
     * @param dto 查询参数
     * @return 司机作业单分页数据
     */
    @PostMapping("/page")
    PageResponse<DriverJobDTO> findByPage(@RequestBody DriverJobDTO dto);

    /**
     * 根据id获取司机作业单信息
     *
     * @param id 司机作业单id
     * @return 司机作业单信息
     */
    @GetMapping("/{id}")
    DriverJobDTO findById(@PathVariable(name = "id") String id);

    /**
     * 根据参数查询全部信息
     *
     * @param dto
     * @return
     */
    @PostMapping("/findAll")
    List<DriverJobDTO> findAll(@RequestBody DriverJobDTO dto);
}
