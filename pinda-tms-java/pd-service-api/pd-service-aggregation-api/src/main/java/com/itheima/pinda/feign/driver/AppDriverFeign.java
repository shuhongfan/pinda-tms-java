package com.itheima.pinda.feign.driver;

import com.itheima.pinda.DTO.AppDriverQueryDTO;
import com.itheima.pinda.DTO.DriverJobDTO;
import com.itheima.pinda.common.utils.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@FeignClient(name = "pd-aggregation")
@RequestMapping("appDriver")
@ApiIgnore
public interface AppDriverFeign {
    /**
     * 分页查询司机任务
     *
     * @param dto
     * @return
     */
    @PostMapping("page")
    PageResponse<DriverJobDTO> findByPage(@RequestBody AppDriverQueryDTO dto);

}
