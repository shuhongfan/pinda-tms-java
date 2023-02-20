package com.itheima.pinda.feign.transportline;

import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.transportline.TransportLineTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("base/transportLine/type")
@ApiIgnore
public interface TransportLineTypeFeign {
    /**
     * 添加线路类型
     *
     * @param dto 线路类型信息
     * @return 线路类型信息
     */
    @PostMapping("")
    TransportLineTypeDto saveTransportLineType(@RequestBody TransportLineTypeDto dto);

    /**
     * 根据id获取线路类型详情
     *
     * @param id 线路类型id
     * @return 线路类型详情
     */
    @GetMapping("/{id}")
    TransportLineTypeDto fineById(@PathVariable(name = "id") String id);

    /**
     * 获取线路类型分页数据
     *
     * @param page       页码
     * @param pageSize   页尺寸
     * @param typeNumber 类型编号
     * @param name       类型名称
     * @param agencyType 机构类型
     * @return 线路类型分页数据
     */
    @GetMapping("/page")
    PageResponse<TransportLineTypeDto> findByPage(@RequestParam(name = "page") Integer page,
                                                  @RequestParam(name = "pageSize") Integer pageSize,
                                                  @RequestParam(name = "typeNumber", required = false) String typeNumber,
                                                  @RequestParam(name = "name", required = false) String name,
                                                  @RequestParam(name = "agencyType", required = false) Integer agencyType);

    /**
     * 获取线路类型列表
     *
     * @param ids 线路类型id列表
     * @return 线路类型列表
     */
    @GetMapping("")
    List<TransportLineTypeDto> findAll(@RequestParam(name = "ids", required = false) List<String> ids);

    /**
     * 更新线路类型信息
     *
     * @param id  线路类型id
     * @param dto 线路类型信息
     * @return 线路类型信息
     */
    @PutMapping("/{id}")
    TransportLineTypeDto update(@PathVariable(name = "id") String id, @RequestBody TransportLineTypeDto dto);

    /**
     * 删除线路类型
     *
     * @param id 线路类型id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    Result disable(@PathVariable(name = "id") String id);
}
