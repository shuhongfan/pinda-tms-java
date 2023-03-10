package com.itheima.pinda.feign.user;

import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.user.CourierScopeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("scope")
@ApiIgnore
public interface CourierScopeFeign {
    /**
     * 批量保存快递员业务范围
     *
     * @param dtoList 快递员业务范围信息
     * @return 返回信息
     */
    @PostMapping("/courier/batch")
    Result batchSaveCourierScope(@RequestBody List<CourierScopeDto> dtoList);

    /**
     * 删除快递员业务范围信息
     *
     * @param dto 参数
     * @return 返回信息
     */
    @DeleteMapping("/courier")
    Result deleteCourierScope(@RequestBody CourierScopeDto dto);

    /**
     * 获取快递员业务范围列表
     *
     * @param areaId 行政区域id
     * @param userId 快递员id
     * @return 快递员业务范围列表
     */
    @GetMapping("/courier")
    List<CourierScopeDto> findAllCourierScope(@RequestParam(name = "areaId", required = false) String areaId, @RequestParam(name = "userId", required = false) String userId);
}
