package com.itheima.pinda.feign;

import com.itheima.pinda.DTO.OrderCargoDto;
import com.itheima.pinda.common.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@FeignClient(name = "pd-oms")
@RequestMapping("cargo")
@ApiIgnore
public interface CargoFeign {
    /**
     * 获取货物列表
     *
     * @param tranOrderId 运单id
     * @param orderId     订单id
     * @return 货物列表
     */
    @GetMapping("")
    List<OrderCargoDto> findAll(@RequestParam(name = "tranOrderId", required = false) String tranOrderId, @RequestParam(name = "orderId", required = false) String orderId);

    /**
     * 添加货物
     *
     * @param dto 货物信息
     * @return 货物信息
     */
    @PostMapping("")
    OrderCargoDto save(@RequestBody OrderCargoDto dto);

    /**
     * 更新货物信息
     *
     * @param id  货物id
     * @param dto 货物信息
     * @return 货物信息
     */
    @PutMapping("/{id}")
    OrderCargoDto update(@PathVariable(name = "id") String id, @RequestBody OrderCargoDto dto);

    /**
     * 删除货物信息
     *
     * @param id 货物id
     * @return 返回信息
     */
    @DeleteMapping("/{id}")
    Result del(@PathVariable(name = "id") String id);

    /**
     * 根据id获取货物详情
     *
     * @param id 货物id
     * @return 货物详情
     */
    @GetMapping("/{id}")
    OrderCargoDto findById(@PathVariable(name = "id") String id);

    /**
     * 批量查询货物信息表
     * @param orderIds
     * @return
     */
    @GetMapping("/list")
    List<OrderCargoDto> list(@RequestParam(name = "orderIds",required = false) List<String> orderIds);
}
