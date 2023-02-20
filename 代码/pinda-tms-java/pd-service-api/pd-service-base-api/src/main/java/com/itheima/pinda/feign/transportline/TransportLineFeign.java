package com.itheima.pinda.feign.transportline;

import com.itheima.pinda.DTO.transportline.TransportLineDto;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("base/transportLine")
@ApiIgnore
public interface TransportLineFeign {
    /**
     * 添加线路
     *
     * @param dto 线路信息
     * @return 线路信息
     */
    @PostMapping("")
    TransportLineDto saveTransportLine(@RequestBody TransportLineDto dto);

    /**
     * 根据id获取线路详情
     *
     * @param id 线路id
     * @return 线路详情
     */
    @GetMapping("/{id}")
    TransportLineDto fineById(@PathVariable(name = "id") String id);

    /**
     * 获取线路分页信息
     *
     * @param page                页码
     * @param pageSize            页尺寸
     * @param lineNumber          线路编号
     * @param name                线路名称
     * @param transportLineTypeId 线路类型id
     * @return 线路分页信息
     */
    @GetMapping("/page")
    PageResponse<TransportLineDto> findByPage(@RequestParam(name = "page") Integer page,
                                              @RequestParam(name = "pageSize") Integer pageSize,
                                              @RequestParam(name = "lineNumber", required = false) String lineNumber,
                                              @RequestParam(name = "name", required = false) String name,
                                              @RequestParam(name = "transportLineTypeId", required = false) String transportLineTypeId);

    /**
     * 获取线路列表
     *
     * @param ids 线路id列表
     * @return 线路列表
     */
    @GetMapping("")
    List<TransportLineDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids,
                                   @RequestParam(name = "agencyId", required = false) String agencyId,
                                   @RequestParam(name = "agencyIds", required = false) List<String> agencyIds);

    /**
     * 更新线路信息
     *
     * @param id  线路id
     * @param dto 线路信息
     * @return 线路信息
     */
    @PutMapping("/{id}")
    TransportLineDto update(@PathVariable(name = "id") String id, @RequestBody TransportLineDto dto);

    /**
     * 删除线路
     *
     * @param id 线路id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    Result disable(@PathVariable(name = "id") String id);

    /**
     * 获取线路列表
     *
     * @return 线路列表
     */
    @PostMapping("list")
    List<TransportLineDto> list(@RequestBody TransportLineDto transportLineDto);
}
