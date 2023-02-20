package com.itheima.pinda.feign;

import com.itheima.pinda.DTO.OrderDTO;
import com.itheima.pinda.DTO.OrderLocationDto;
import com.itheima.pinda.DTO.OrderSearchDTO;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@FeignClient("pd-oms")
@RequestMapping("order")
@ApiIgnore
public interface OrderFeign {
    /**
     * 新增订单
     *
     * @param orderDTO 订单信息
     * @return 订单信息
     */
    @PostMapping("")
    OrderDTO save(@RequestBody OrderDTO orderDTO);

    /**
     * 修改订单信息
     *
     * @param id       订单id
     * @param orderDTO 订单信息
     * @return 订单信息
     */
    @PutMapping("/{id}")
    OrderDTO updateById(@PathVariable(name = "id") String id, @RequestBody OrderDTO orderDTO);

    /**
     * 获取订单分页数据
     *
     * @param orderDTO 查询条件
     * @return 订单分页数据
     */
    @PostMapping("/page")
    PageResponse<OrderDTO> findByPage(@RequestBody OrderDTO orderDTO);

    /**
     * 根据id获取订单详情
     *
     * @param id 订单Id
     * @return 订单详情
     */
    @GetMapping("/{id}")
    OrderDTO findById(@PathVariable(name = "id") String id);

    /**
     * 客户端订单模糊搜索
     *
     * @param orderSearchDTO
     * @return
     */
    @RequestMapping(value = "pageLikeForCustomer", method = RequestMethod.POST)
    PageResponse<OrderDTO> pageLikeForCustomer(@RequestBody OrderSearchDTO orderSearchDTO);

    /**
     * 获取id集合
     *
     * @param ids
     * @return
     */
    @GetMapping("ids")
    List<OrderDTO> findByIds(@RequestParam("ids") List<String> ids);

    @PostMapping("list")
    List<Order> list(@RequestBody OrderSearchDTO orderSearchDTO);

    @PostMapping("orderMsg")
    Map getOrderMsg(OrderDTO orderAddDto);

    @PostMapping("location/saveOrUpdate")
    OrderLocationDto saveOrUpdateLoccation(@RequestBody OrderLocationDto orderLocationDto);

    @GetMapping("orderId")
    OrderLocationDto selectByOrderId(@RequestParam(name = "orderId") String orderId);

    @PostMapping("del")
    int deleteOrderLocation(@RequestBody OrderLocationDto orderLocationDto);

    @PostMapping("omsSeataTest")
    OrderDTO omsSeataTest(@RequestBody OrderDTO orderDTO);

}