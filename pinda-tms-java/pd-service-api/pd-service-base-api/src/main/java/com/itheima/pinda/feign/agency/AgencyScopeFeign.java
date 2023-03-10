package com.itheima.pinda.feign.agency;

import com.itheima.pinda.DTO.angency.AgencyScopeDto;
import com.itheima.pinda.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("scope")
@ApiIgnore
public interface AgencyScopeFeign {
    /**
     * 批量保存机构业务范围
     *
     * @param dtoList 机构业务范围信息
     * @return 返回信息
     */
    @PostMapping("/agency/batch")
    Result batchSaveAgencyScope(@RequestBody List<AgencyScopeDto> dtoList);

    /**
     * 删除机构业务范围信息
     *
     * @param dto 参数
     * @return 返回信息
     */
    @DeleteMapping("/agency")
    Result deleteAgencyScope(@RequestBody AgencyScopeDto dto);

    /**
     * 获取机构业务范围列表
     *
     * @param areaId   行政区域id
     * @param agencyId 机构id
     * @return 机构业务范围列表
     */
    @GetMapping("/agency")
    List<AgencyScopeDto> findAllAgencyScope(@RequestParam(name = "areaId", required = false) String areaId, @RequestParam(name = "agencyId", required = false) String agencyId, @RequestParam(name = "agencyIds", required = false) List<String> agencyIds, @RequestParam(name = "areaIds", required = false) List<String> areaIds);
}
