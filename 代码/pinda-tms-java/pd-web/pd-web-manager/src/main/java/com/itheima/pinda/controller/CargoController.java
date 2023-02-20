package com.itheima.pinda.controller;

import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.DTO.OrderCargoDto;
import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.feign.CargoFeign;
import com.itheima.pinda.feign.common.GoodsTypeFeign;
import com.itheima.pinda.vo.oms.OrderCargoVo;
import com.itheima.pinda.vo.oms.OrderVo;
import com.itheima.pinda.vo.base.businessHall.GoodsTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("order-manager/cargo")
@Api(tags = "货品管理")
@Log
public class CargoController {
    @Autowired
    private CargoFeign cargoFeign;
    @Autowired
    private GoodsTypeFeign goodsTypeFeign;

    @ApiOperation(value = "添加货物")
    @PostMapping("")
    public OrderCargoVo saveOrderCargo(@RequestBody OrderCargoVo vo) {
        OrderCargoDto resultDto = cargoFeign.save(parseOderCargoVo2Dto(vo));
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "获取货物列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, example = "0")
    })
    @GetMapping(value = "")
    public List<OrderCargoVo> findAll(@RequestParam(value = "orderId") String orderId) {
        List<OrderCargoDto> cargoDtoList = cargoFeign.findAll(null, orderId);
        return cargoDtoList.stream().map(orderCargoDto -> {
            OrderCargoVo vo = new OrderCargoVo();
            BeanUtils.copyProperties(orderCargoDto, vo);
            if (StringUtils.isNotEmpty(orderCargoDto.getGoodsTypeId())) {
                GoodsTypeDto goodTypeDto = goodsTypeFeign.fineById(orderCargoDto.getGoodsTypeId());
                if (goodTypeDto != null) {
                    GoodsTypeVo goodsTypeVo = new GoodsTypeVo();
                    BeanUtils.copyProperties(goodTypeDto, goodsTypeVo);
                    vo.setGoodsType(goodsTypeVo);
                }
            }
            if (StringUtils.isNotEmpty(orderCargoDto.getOrderId())) {
                OrderVo orderVo = new OrderVo();
                orderVo.setId(orderCargoDto.getOrderId());
                vo.setOrder(orderVo);
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @ApiOperation(value = "更新货物信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "货物id", required = true, example = "1", paramType = "{path}")
    })
    @PutMapping("/{id}")
    public OrderCargoVo updateOrderCargo(@PathVariable(name = "id") String id, @RequestBody OrderCargoVo vo) {
        vo.setId(id);
        OrderCargoDto resultDto = cargoFeign.update(id, parseOderCargoVo2Dto(vo));
        BeanUtils.copyProperties(resultDto, vo);
        return vo;
    }

    @ApiOperation(value = "删除货物")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "货物id", required = true, example = "1", paramType = "{path}")
    })
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(name = "id") String id) {
        cargoFeign.del(id);
        return Result.ok();
    }

    /**
     * 货物数据转换
     *
     * @param vo 货物数据
     * @return 货物数据
     */
    private OrderCargoDto parseOderCargoVo2Dto(OrderCargoVo vo) {
        OrderCargoDto dto = new OrderCargoDto();
        BeanUtils.copyProperties(vo, dto);
        if (vo.getGoodsType() != null) {
            dto.setGoodsTypeId(vo.getGoodsType().getId());
        }
        if (vo.getOrder() != null) {
            dto.setOrderId(vo.getOrder().getId());
        }
        return dto;
    }
}
