package com.itheima.pinda.feign;

import com.itheima.pinda.DTO.TransportOrderDTO;
import com.itheima.pinda.DTO.TransportOrderSearchDTO;
import com.itheima.pinda.common.utils.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 运单相关Api
 *
 * @author jpfss
 */
@FeignClient("pd-work")
@RequestMapping("transport-order")
public interface TransportOrderFeign {
    /**
     * 新增运单
     *
     * @param dto 运单信息
     * @return 运单信息
     */
    @PostMapping("")
    TransportOrderDTO save(@RequestBody TransportOrderDTO dto);

    /**
     * 修改运单信息
     *
     * @param id  运单id
     * @param dto 运单信息
     * @return 运单信息
     */
    @PutMapping("/{id}")
    TransportOrderDTO updateById(@PathVariable(name = "id") String id, @RequestBody TransportOrderDTO dto);

    /**
     * 获取运单分页数据
     *
     * @param page             页码
     * @param pageSize         页尺寸
     * @param orderId          订单ID
     * @param status           运单状态(1.新建 2.已装车，发往x转运中心 3.到达 4.到达终端网点)
     * @param schedulingStatus 调度状态调度状态(1.待调度2.未匹配线路3.已调度)
     * @return 运单分页数据
     */
    @GetMapping("/page")
    PageResponse<TransportOrderDTO> findByPage(@RequestParam(name = "page") Integer page,
                                               @RequestParam(name = "pageSize") Integer pageSize,
                                               @RequestParam(name = "orderId", required = false) String orderId,
                                               @RequestParam(name = "status", required = false) Integer status,
                                               @RequestParam(name = "schedulingStatus", required = false) Integer schedulingStatus);

    /**
     * 根据id获取运单信息
     *
     * @param id 运单id
     * @return 运单信息
     */
    @GetMapping("/{id}")
    TransportOrderDTO findById(@PathVariable(name = "id") String id);

    /**
     * 根据订单id获取运单信息
     *
     * @param orderId 订单id
     * @return 运单信息
     */
    @GetMapping("/orderId/{orderId}")
    TransportOrderDTO findByOrderId(@PathVariable(name = "orderId") String orderId);

    /**
     * 根据多个订单id批量获取运单信息
     *
     * @param ids
     * @return
     */
    @GetMapping("orderIds")
    List<TransportOrderDTO> findByOrderIds(@RequestParam(name = "ids", required = false) List<String> ids);

    /**
     * 根据多个参数获取运单信息
     *
     * @param transportOrderSearchDTO
     * @return
     */
    @PostMapping("list")
    List<TransportOrderDTO> list(@RequestBody TransportOrderSearchDTO transportOrderSearchDTO);
}
