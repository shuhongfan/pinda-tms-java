package com.itheima.pinda.feign.common;

import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.base.GoodsTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-base")
@RequestMapping("base/goodsType")
@ApiIgnore
public interface GoodsTypeFeign {
    /**
     * 添加货物类型
     *
     * @param dto 货物类型信息
     * @return 货物类型信息
     */
    @PostMapping("")
    GoodsTypeDto saveGoodsType(@RequestBody GoodsTypeDto dto);

    /**
     * 根据id获取货物类型详情
     *
     * @param id 货物类型id
     * @return 货物类型信息
     */
    @GetMapping("/{id}")
    GoodsTypeDto fineById(@PathVariable(name = "id") String id);

    /**
     * 获取货物类型列表
     *
     * @return 货物类型列表
     */
    @GetMapping("")
    public List<GoodsTypeDto> findAll(@RequestParam(name = "ids",required = false) List<String> ids);

    @GetMapping("all")
    public List<GoodsTypeDto> findAll();

    /**
     * 获取分页货物类型数据
     *
     * @param page     页码
     * @param pageSize 页尺寸
     * @return 分页货物类型数据
     */
    @GetMapping("/page")
    PageResponse<GoodsTypeDto> findByPage(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "pageSize") Integer pageSize,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "truckTypeId", required = false) String truckTypeId,
            @RequestParam(name = "truckTypeName", required = false) String truckTypeName);

    /**
     * 更新货物类型信息
     *
     * @param dto 货物类型信息
     * @return 货物类型信息
     */
    @PutMapping("/{id}")
    GoodsTypeDto update(@PathVariable(name = "id") String id, @RequestBody GoodsTypeDto dto);

    /**
     * 删除货物类型
     *
     * @param id 货物类型id
     * @return 返回信息
     */
    @PutMapping("/{id}/disable")
    Result disable(@PathVariable(name = "id") String id);
}
