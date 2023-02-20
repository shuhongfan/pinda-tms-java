package com.itheima.pinda.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;
import com.itheima.pinda.DTO.OrderDTO;
import com.itheima.pinda.DTO.OrderLocationDto;
import com.itheima.pinda.DTO.OrderSearchDTO;
import com.itheima.pinda.common.utils.CustomIdGenerator;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.entity.Order;
import com.itheima.pinda.entity.OrderLocation;
import com.itheima.pinda.service.IOrderLocationService;
import com.itheima.pinda.service.IOrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Log4j2
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderLocationService orderLocationService;
    @Autowired
    private CustomIdGenerator idGenerator;

    /**
     * 新增订单
     *
     * @param orderDTO 订单信息
     * @return 订单信息
     */
    @PostMapping("")
    public OrderDTO save(@RequestBody OrderDTO orderDTO, HttpServletResponse res) {
        log.info("保存订单信息:{}", JSON.toJSONString(orderDTO));
        Order order = new Order();
        order.setEstimatedArrivalTime(LocalDateTime.now().plus(2, ChronoUnit.DAYS));
        Map map = orderService.calculateAmount(orderDTO);
        log.info("实时计算运费:{}", map);
        orderDTO = (OrderDTO) map.get("orderDto");
        BeanUtils.copyProperties(orderDTO, order);
        if ("send error msg".equals(orderDTO.getSenderAddress()) || "receive error msg".equals(orderDTO.getReceiverAddress())) {
            return orderDTO;
        }
        order.setAmount(new BigDecimal(map.getOrDefault("amount", "23").toString()));
        orderService.saveOrder(order);
        log.info("订单信息入库:{}", order);
        OrderDTO result = new OrderDTO();
        BeanUtils.copyProperties(order, result);
        return result;
    }


    @PostMapping("orderMsg")
    public Map save(@RequestBody OrderDTO orderDTO) {
        Map map = Maps.newHashMap();
        if (orderDTO == null) {
            return map;
        }
        if (orderDTO.getOrderCargoDto() == null) {
            return map;
        }
        BigDecimal bigDecimal = orderDTO.getOrderCargoDto().getTotalWeight() == null ? BigDecimal.ZERO : orderDTO.getOrderCargoDto().getTotalWeight();
        if (bigDecimal.compareTo(BigDecimal.ZERO) < 1) {
            return map;
        }
        //根据重量和距离
        map = orderService.calculateAmount(orderDTO);
        return map;
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


    @ResponseBody
    @PostMapping("location/saveOrUpdate")
    public OrderLocationDto saveOrUpdateLoccation(@RequestBody OrderLocationDto orderLocationDto) {
        String id = orderLocationDto.getId();
        String orderId = orderLocationDto.getOrderId();
        if (StringUtils.isNotBlank(id)) {
            OrderLocation location = orderLocationService.getBaseMapper().selectOne(new QueryWrapper<OrderLocation>().eq("order_id", orderId).last(" limit 1"));
            if (location != null) {
                OrderLocation orderLocationUpdate = new OrderLocation();
                BeanUtils.copyProperties(orderLocationDto, orderLocationUpdate);
                orderLocationUpdate.setId(location.getId());
                orderLocationService.getBaseMapper().updateById(orderLocationUpdate);
                BeanUtils.copyProperties(orderLocationUpdate, orderLocationDto);
            }
        } else {
            OrderLocation orderLocation = new OrderLocation();
            BeanUtils.copyProperties(orderLocationDto, orderLocation);
            orderLocation.setId(idGenerator.nextId(orderLocation).toString());
            orderLocationService.save(orderLocation);
            BeanUtils.copyProperties(orderLocation, orderLocationDto);
        }

        return orderLocationDto;
    }

    @GetMapping("orderId")
    public OrderLocationDto selectByOrderId(@RequestParam(name = "orderId") String orderId) {
        OrderLocationDto result = new OrderLocationDto();
        OrderLocation location = orderLocationService.getBaseMapper().selectOne(new QueryWrapper<OrderLocation>().eq("order_id", orderId).last(" limit 1"));
        BeanUtils.copyProperties(location, result);
        return result;
    }

    @PostMapping("del")
    public int deleteOrderLocation(@RequestBody OrderLocationDto orderLocationDto) {
        String orderId = orderLocationDto.getOrderId();
        int result = 0;
        if (StringUtils.isNotBlank(orderId)) {
            result = orderLocationService.getBaseMapper().delete(new UpdateWrapper<OrderLocation>().eq("order_id", orderLocationDto));
        }
        return result;
    }
}

