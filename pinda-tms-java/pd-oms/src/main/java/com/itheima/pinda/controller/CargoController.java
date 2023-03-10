package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.pinda.DTO.OrderCargoDto;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.OrderCargo;
import com.itheima.pinda.service.IOrderCargoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 货物
 */
@RestController
@RequestMapping("cargo")
public class CargoController {
    @Autowired
    private IOrderCargoService orderCargoService;

    /**
     * 获取货物列表
     *
     * @param tranOrderId 运单id
     * @param orderId     订单id
     * @return 货物列表
     */
    @GetMapping("")
    public List<OrderCargoDto> findAll(@RequestParam(name = "tranOrderId", required = false) String tranOrderId, @RequestParam(name = "orderId", required = false) String orderId) {
        return orderCargoService.findAll(tranOrderId, orderId).stream().map(orderCargo -> {
            OrderCargoDto cargoDto = new OrderCargoDto();
            BeanUtils.copyProperties(orderCargo, cargoDto);
            return cargoDto;
        }).collect(Collectors.toList());
    }

    @GetMapping("/list")
    public List<OrderCargoDto> list(@RequestParam(name = "orderIds", required = false) List<String> orderIds) {
        LambdaQueryWrapper<OrderCargo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(!CollectionUtils.isEmpty(orderIds), OrderCargo::getOrderId, orderIds);

        return orderCargoService.list(wrapper).stream().map(orderCargo -> {
            OrderCargoDto cargoDto = new OrderCargoDto();
            BeanUtils.copyProperties(orderCargo, cargoDto);
            return cargoDto;
        }).collect(Collectors.toList());
    }

    /**
     * 添加货物
     *
     * @param dto 货物信息
     * @return 货物信息
     */
    @PostMapping("")
    public OrderCargoDto save(@RequestBody OrderCargoDto dto) {
        OrderCargo orderCargo = new OrderCargo();
        BeanUtils.copyProperties(dto, orderCargo);
        orderCargo = orderCargoService.saveSelective(orderCargo);
        BeanUtils.copyProperties(orderCargo, dto);
        return dto;
    }

    /**
     * 更新货物信息
     *
     * @param id  货物id
     * @param dto 货物信息
     * @return 货物信息
     */
    @PutMapping("/{id}")
    public OrderCargoDto update(@PathVariable(name = "id") String id, @RequestBody OrderCargoDto dto) {
        dto.setId(id);
        OrderCargo orderCargo = new OrderCargo();
        BeanUtils.copyProperties(dto, orderCargo);
        orderCargoService.updateById(orderCargo);
        return dto;
    }

    /**
     * 删除货物信息
     *
     * @param id 货物id
     * @return 返回信息
     */
    @DeleteMapping("/{id}")
    public Result del(@PathVariable(name = "id") String id) {
        orderCargoService.removeById(id);
        return Result.ok();
    }


    /**
     * 根据id获取货物详情
     *
     * @param id 货物id
     * @return 货物详情
     */
    @GetMapping("/{id}")
    public OrderCargoDto findById(@PathVariable(name = "id") String id) {
        OrderCargo orderCargo = orderCargoService.getById(id);
        OrderCargoDto dto = new OrderCargoDto();
        BeanUtils.copyProperties(orderCargo, dto);
        return dto;
    }

}
