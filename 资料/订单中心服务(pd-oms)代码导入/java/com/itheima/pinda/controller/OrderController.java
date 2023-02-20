package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.DTO.OrderDTO;
import com.itheima.pinda.DTO.OrderSearchDTO;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.entity.Order;
import com.itheima.pinda.service.IOrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单  前端控制器
 * </p>
 *
 * @author jpf
 * @since 2019-12-26
 */
@Log4j2
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    /**
     * 新增订单
     *
     * @param orderDTO 订单信息
     * @return 订单信息
     */
    @PostMapping("")
    public OrderDTO save(@RequestBody OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        // TODO: 2020/4/2 预计到达时间待实现,暂时实现为两天后
        order.setEstimatedArrivalTime(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        // TODO: 2020/4/2 距离待实现,暂时实现为3公里
        order.setDistance(new BigDecimal(3.00));
        orderService.saveOrder(order);
        OrderDTO result = new OrderDTO();
        BeanUtils.copyProperties(order, result);
        return result;
    }

    /**
     * 修改订单信息
     *
     * @param id       订单id
     * @param orderDTO 订单信息
     * @return 订单信息
     */
    @PutMapping("/{id}")
    public OrderDTO updateById(@PathVariable(name = "id") String id, @RequestBody OrderDTO orderDTO) {
        orderDTO.setId(id);
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        orderService.updateById(order);
        return orderDTO;
    }

    /**
     * 获取订单分页数据
     *
     * @param orderDTO 查询条件
     * @return 订单分页数据
     */
    @PostMapping("/page")
    public PageResponse<OrderDTO> findByPage(@RequestBody OrderDTO orderDTO) {
        Order queryOrder = new Order();
        BeanUtils.copyProperties(orderDTO, queryOrder);
        IPage<Order> orderIPage = orderService.findByPage(orderDTO.getPage(), orderDTO.getPageSize(), queryOrder);
        List<OrderDTO> dtoList = new ArrayList<>();
        orderIPage.getRecords().forEach(order -> {
            OrderDTO dto = new OrderDTO();
            BeanUtils.copyProperties(order, dto);
            dtoList.add(dto);
        });
        return PageResponse.<OrderDTO>builder().items(dtoList).pagesize(orderDTO.getPageSize()).page(orderDTO.getPage()).counts(orderIPage.getTotal())
                .pages(orderIPage.getPages()).build();
    }

    /**
     * 根据id获取订单详情
     *
     * @param id 订单Id
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public OrderDTO findById(@PathVariable(name = "id") String id) {
        OrderDTO orderDTO = new OrderDTO();
        Order order = orderService.getById(id);

        if (order != null) {
            BeanUtils.copyProperties(order, orderDTO);
        } else {
            orderDTO = null;
        }
        return orderDTO;
    }

    /**
     * 根据id获取集合
     *
     * @param ids 订单Id
     * @return 订单详情
     */
    @GetMapping("ids")
    public List<OrderDTO> findById(@RequestParam(name = "ids") List<String> ids) {
        List<Order> orders = orderService.listByIds(ids);
        return orders.stream().map(item -> {
            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(item, orderDTO);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "pageLikeForCustomer", method = RequestMethod.POST)
    public PageResponse<OrderDTO> pageLikeForCustomer(@RequestBody OrderSearchDTO orderSearchDTO) {

        //查询结果
        IPage<Order> orderIPage = orderService.pageLikeForCustomer(orderSearchDTO);
        List<OrderDTO> dtoList = new ArrayList<>();
        orderIPage.getRecords().forEach(order -> {
            OrderDTO dto = new OrderDTO();
            BeanUtils.copyProperties(order, dto);
            dtoList.add(dto);
        });

        return PageResponse.<OrderDTO>builder().items(dtoList).pagesize(orderSearchDTO.getPageSize()).page(orderSearchDTO.getPage()).counts(orderIPage.getTotal())
                .pages(orderIPage.getPages()).build();
    }

    @ResponseBody
    @PostMapping("list")
    public List<Order> list(@RequestBody OrderSearchDTO orderSearchDTO) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(orderSearchDTO.getStatus() != null, Order::getStatus, orderSearchDTO.getStatus());
        wrapper.in(!CollectionUtils.isEmpty(orderSearchDTO.getReceiverCountyIds()), Order::getReceiverCountyId, orderSearchDTO.getReceiverCountyIds());
        wrapper.in(!CollectionUtils.isEmpty(orderSearchDTO.getSenderCountyIds()), Order::getSenderCountyId, orderSearchDTO.getSenderCountyIds());
        wrapper.eq(StringUtils.isNotEmpty(orderSearchDTO.getCurrentAgencyId()), Order::getCurrentAgencyId, orderSearchDTO.getCurrentAgencyId());

        return orderService.list(wrapper);
    }
}
