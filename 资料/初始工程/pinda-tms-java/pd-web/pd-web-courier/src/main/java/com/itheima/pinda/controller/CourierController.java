package com.itheima.pinda.controller;


import com.itheima.pinda.DTO.*;
import com.itheima.pinda.DTO.base.GoodsTypeDto;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.common.context.RequestContext;
import com.itheima.pinda.common.utils.IdCardUtils;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.Member;
import com.itheima.pinda.enums.MemberIdCardVerifyStatus;
import com.itheima.pinda.enums.OrderStatus;
import com.itheima.pinda.enums.OrderType;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskSignStatus;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskStatus;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskType;
import com.itheima.pinda.enums.transportorder.TransportOrderSchedulingStatus;
import com.itheima.pinda.enums.transportorder.TransportOrderStatus;
import com.itheima.pinda.feign.*;
import com.itheima.pinda.feign.common.GoodsTypeFeign;
import com.itheima.pinda.feign.courier.AppCourierFeign;
import com.itheima.pinda.future.PdCompletableFuture;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 运单表 前端控制器
 * </p>
 *
 * @author diesel
 * @since 2020-03-24
 */
@Slf4j
@Api(tags = "快递员业务")
@Controller
@RequestMapping("courier")
public class CourierController {

    private final PickupDispatchTaskFeign pickupDispatchTaskFeign;

    private final OrderFeign orderFeign;

    private final CargoFeign cargoFeign;

    private final AreaApi areaApi;

    private final GoodsTypeFeign goodsTypeFeign;

    private final TransportOrderFeign transportOrderFeign;

    private final TransportTaskFeign transportTaskFeign;

    private final OrgApi orgApi;

    private final MemberFeign memberFeign;

    private final AppCourierFeign appCourierFeign;


    public CourierController(AppCourierFeign appCourierFeign, MemberFeign memberFeign, OrgApi orgApi, TransportTaskFeign transportTaskFeign, TransportOrderFeign transportOrderFeign, GoodsTypeFeign goodsTypeFeign, PickupDispatchTaskFeign pickupDispatchTaskFeign, OrderFeign orderFeign, CargoFeign cargoFeign, AreaApi areaApi) {
        this.appCourierFeign = appCourierFeign;
        this.memberFeign = memberFeign;
        this.pickupDispatchTaskFeign = pickupDispatchTaskFeign;
        this.goodsTypeFeign = goodsTypeFeign;
        this.orderFeign = orderFeign;
        this.cargoFeign = cargoFeign;
        this.areaApi = areaApi;
        this.transportOrderFeign = transportOrderFeign;
        this.transportTaskFeign = transportTaskFeign;
        this.orgApi = orgApi;
    }

    @SneakyThrows
    @ApiOperation(value = "待取件/待妥投")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", required = true, example = "1"),
            @ApiImplicitParam(name = "pagesize", value = "每页条数", required = true, example = "10"),
            @ApiImplicitParam(name = "taskType", value = "类型", required = true, example = ""),
            @ApiImplicitParam(name = "status", value = "状态", required = true, example = ""),
            @ApiImplicitParam(name = "keyword", value = "搜索条件", required = false, example = ""),
            @ApiImplicitParam(name = "date", value = "时间", required = false, example = "")
            //@ApiImplicitParam(name = "statusArray", value = "批量状态", required = false, example = "")
    })
    @ResponseBody
    @GetMapping("pickupDispatch")
    public Result pickupDispatch(Integer page, Integer pagesize, Integer taskType, Integer status, String keyword, String date) {

        //  快递员id  并放入参数
        String courierId = RequestContext.getUserId();

        AppCourierQueryDTO appCourierQueryDTO = new AppCourierQueryDTO();
        appCourierQueryDTO.setPage(page);
        appCourierQueryDTO.setPageSize(pagesize);
        appCourierQueryDTO.setCourierId(courierId);
        //状态在 PickupDispatchTaskStatus 中
        appCourierQueryDTO.setStatus(status);
        //类型在 PickupDispatchTaskType 中
        appCourierQueryDTO.setTaskType(taskType);
        if (StringUtils.isNotBlank(keyword)) {
            appCourierQueryDTO.setKeyword(keyword);
        }
        if (StringUtils.isNotEmpty(date)) {
            appCourierQueryDTO.setDate(date);
        }
        log.info("查询任务信息：{}", appCourierQueryDTO);
        PageResponse<TaskPickupDispatchDTO> result = appCourierFeign.findByPage(appCourierQueryDTO);
        if (result.getItems() == null || result.getItems().size() == 0) {
            return Result.ok().put("data", PageResponse.<PickupDispatchDTO>builder().page(result.getPage()).pagesize(result.getPagesize()).pages(result.getPages()).counts(result.getCounts()).build());
        }
        log.info("查询到任务信息：{}", result.getItems());
        // 构建orderId集合
        Set<String> orderSet = result.getItems().stream().map(item -> item.getOrderId()).collect(Collectors.toSet());
        //查询订单信息
        CompletableFuture<Map<String, OrderDTO>> orderMapFuture = PdCompletableFuture.orderMapFuture(orderFeign, orderSet);
        // 查询运单信息
        CompletableFuture<Map<String, TransportOrderDTO>> tranOrderMapFuture = PdCompletableFuture.tranOrderMapFuture(transportOrderFeign, orderSet);

        Map<String, OrderDTO> orderMap = orderMapFuture.get();
        log.info("根据任务信息获取订单数据：{}，result:{}", orderSet, orderMap);
        Collection<OrderDTO> orderDTOs = orderMap.values();

        //查询地址信息
        Set<Long> addressSet = new HashSet<>();
        addressSet.addAll(orderDTOs.stream().filter(item -> item.getReceiverProvinceId() != null).map(item -> Long.valueOf(item.getReceiverProvinceId())).collect(Collectors.toSet()));
        addressSet.addAll(orderDTOs.stream().filter(item -> item.getReceiverCityId() != null).map(item -> Long.valueOf(item.getReceiverCityId())).collect(Collectors.toSet()));
        addressSet.addAll(orderDTOs.stream().filter(item -> item.getReceiverCountyId() != null).map(item -> Long.valueOf(item.getReceiverCountyId())).collect(Collectors.toSet()));
        addressSet.addAll(orderDTOs.stream().filter(item -> item.getSenderProvinceId() != null).map(item -> Long.valueOf(item.getSenderProvinceId())).collect(Collectors.toSet()));
        addressSet.addAll(orderDTOs.stream().filter(item -> item.getSenderCityId() != null).map(item -> Long.valueOf(item.getSenderCityId())).collect(Collectors.toSet()));
        addressSet.addAll(orderDTOs.stream().filter(item -> item.getSenderCountyId() != null).map(item -> Long.valueOf(item.getSenderCountyId())).collect(Collectors.toSet()));
        CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, addressSet);

        Map<String, TransportOrderDTO> tranOrderMap = tranOrderMapFuture.get();
        log.info("根据任务信息获取运单数据：{}，result:{}", orderSet, tranOrderMap);
        Map<Long, Area> areaMap = areaMapFuture.get();

        List<PickupDispatchDTO> pickupDispatchDtos = result.getItems().stream().map(item -> new PickupDispatchDTO(item, tranOrderMap, orderMap, areaMap)).collect(Collectors.toList());
        return Result.ok().put("data", PageResponse.<PickupDispatchDTO>builder()
                .page(result.getPage())
                .pagesize(result.getPagesize())
                .pages(result.getPages()).counts(result.getCounts())
                .items(pickupDispatchDtos)
                .build());
    }

    @ApiOperation(value = "待取件/待妥投 数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskType", value = "类型", required = true, example = ""),
            @ApiImplicitParam(name = "status", value = "状态", required = true, example = ""),
            @ApiImplicitParam(name = "keyword", value = "搜索条件", required = false, example = ""),
            @ApiImplicitParam(name = "date", value = "时间", required = false, example = "")
//            @ApiImplicitParam(name = "statusArray", value = "批量状态", required = false, example = "")
    })
    @ResponseBody
    @GetMapping("count")
    public Result count(Integer taskType, Integer status, String keyword, String date) {
        //  快递员id  并放入参数
        String courierId = RequestContext.getUserId();

        AppCourierQueryDTO appCourierQueryDTO = new AppCourierQueryDTO();
        appCourierQueryDTO.setPage(1);
        appCourierQueryDTO.setPageSize(1);
        appCourierQueryDTO.setCourierId(courierId);
        //状态在 PickupDispatchTaskStatus 中
        appCourierQueryDTO.setStatus(status);
        //类型在 PickupDispatchTaskType 中
        appCourierQueryDTO.setTaskType(taskType);
        if (StringUtils.isNotBlank(keyword)) {
            appCourierQueryDTO.setKeyword(keyword);
        }
        if (StringUtils.isNotEmpty(date)) {
            appCourierQueryDTO.setDate(date);
        }

        PageResponse<TaskPickupDispatchDTO> result = appCourierFeign.findByPage(appCourierQueryDTO);
        if (result.getItems() == null || result.getItems().size() == 0) {
            Result.ok().put("count", 0);
        }
        log.info("查询数量 params:{} result：{}", appCourierQueryDTO, result.getCounts());
        return Result.ok().put("count", result.getCounts());
    }

    @SneakyThrows
    @ApiOperation(value = "详情页")
    @ApiImplicitParam(name = "id", value = "主键", required = true, example = "")
    @ResponseBody
    @GetMapping("detail")
    public Result detail(String id) {
        log.info("任务详情:{}", id);
        TaskPickupDispatchDTO pickupDispatchTaskDTO = pickupDispatchTaskFeign.findById(id);
        String orderId = pickupDispatchTaskDTO.getOrderId();
        OrderDTO orderDTO = orderFeign.findById(orderId);
        List<OrderCargoDto> orderCargoDtos = cargoFeign.findAll(null, orderDTO.getId());
        OrderCargoDto orderCargoDto = orderCargoDtos.get(0);

        TransportOrderDTO transportOrder = transportOrderFeign.findByOrderId(orderId);
        log.info("查询运单信息：{},RESULT:{}", orderId, transportOrder);

        Set<Long> addressSet = new HashSet<>();
        if (StringUtils.isNotEmpty(orderDTO.getReceiverProvinceId())) {
            addressSet.add(Long.valueOf(orderDTO.getReceiverProvinceId()));
        }
        if (StringUtils.isNotEmpty(orderDTO.getReceiverCityId())) {
            addressSet.add(Long.valueOf(orderDTO.getReceiverCityId()));
        }
        if (StringUtils.isNotEmpty(orderDTO.getReceiverCountyId())) {
            addressSet.add(Long.valueOf(orderDTO.getReceiverCountyId()));
        }
        if (StringUtils.isNotEmpty(orderDTO.getSenderProvinceId())) {
            addressSet.add(Long.valueOf(orderDTO.getSenderProvinceId()));
        }
        if (StringUtils.isNotEmpty(orderDTO.getSenderCityId())) {
            addressSet.add(Long.valueOf(orderDTO.getSenderCityId()));
        }
        if (StringUtils.isNotEmpty(orderDTO.getSenderCountyId())) {
            addressSet.add(Long.valueOf(orderDTO.getSenderCountyId()));
        }
        CompletableFuture<Map<Long, Area>> areaMapFuture = PdCompletableFuture.areaMapFuture(areaApi, null, addressSet);
        Map<Long, Area> areaMap = areaMapFuture.get();
        log.info("查询物品类型：{}", orderCargoDto.getGoodsTypeId());
        GoodsTypeDto goodsType = null;
        if (StringUtils.isNotBlank(orderCargoDto.getGoodsTypeId())) {
            goodsType = goodsTypeFeign.fineById(orderCargoDto.getGoodsTypeId());
        }
        log.info("查询物品类型：{},RESULT:{}", orderCargoDto.getGoodsTypeId(), goodsType);

        Member member = memberFeign.detail(orderDTO.getMemberId());
        log.info("查询发件人信息：{}", member);
        return Result.ok().put("data", new PickupDispatchDetailDTO(pickupDispatchTaskDTO, orderDTO, orderCargoDto, goodsType, areaMap, transportOrder, member));
    }

    @SneakyThrows
    @ApiOperation(value = "揽收")
    @ApiImplicitParam(name = "id", value = "主键", required = true, example = "")
    @ResponseBody
    @PutMapping("detail/{id}")
    public Result detail(@PathVariable("id") String id, @RequestBody PickupDispatchDetailDTO pickupDispatchDetailDTO) {
        log.info("揽收：{},{}", id, pickupDispatchDetailDTO);
        pickupDispatchDetailDTO.getGoodsTypeId();
        if (null != pickupDispatchDetailDTO.getPaymentMethod()) {
            OrderDTO orderEditDTO = new OrderDTO();
            orderEditDTO.setId(pickupDispatchDetailDTO.getOrderNumber());
            orderEditDTO.setPaymentMethod(pickupDispatchDetailDTO.getPaymentMethod());
            orderEditDTO.setStatus(OrderStatus.PICKED_UP.getCode());
            orderFeign.updateById(orderEditDTO.getId(), orderEditDTO);
        }

        List<OrderCargoDto> orderCargoDtos = cargoFeign.findAll(null, pickupDispatchDetailDTO.getOrderNumber());
        log.info("揽收-订单附属信息：{},{}", pickupDispatchDetailDTO.getOrderNumber(), orderCargoDtos);
        OrderCargoDto orderCargoDto = orderCargoDtos.get(0);
        if (StringUtils.isNotEmpty(pickupDispatchDetailDTO.getGoodsTypeId())) {
            orderCargoDto.setGoodsTypeId(pickupDispatchDetailDTO.getGoodsTypeId());
        }
        if (StringUtils.isNotEmpty(pickupDispatchDetailDTO.getSustenance())) {
            orderCargoDto.setName(pickupDispatchDetailDTO.getSustenance());
        }
        if (pickupDispatchDetailDTO.getVolume() != null) {
            orderCargoDto.setVolume(pickupDispatchDetailDTO.getVolume());
            orderCargoDto.setTotalVolume(orderCargoDto.getVolume().multiply(new BigDecimal(orderCargoDto.getQuantity())));
        }
        if (pickupDispatchDetailDTO.getWeight() != null) {
            orderCargoDto.setWeight(pickupDispatchDetailDTO.getWeight());
            orderCargoDto.setTotalWeight(orderCargoDto.getWeight().multiply(new BigDecimal(orderCargoDto.getQuantity())));
        }

        log.info("揽收-修改物品附属信息:{},{}", orderCargoDto.getId(), orderCargoDto);
        cargoFeign.update(orderCargoDto.getId(), orderCargoDto);
        TaskPickupDispatchDTO taskPickupDispatchDTO = new TaskPickupDispatchDTO();
        taskPickupDispatchDTO.setStatus(PickupDispatchTaskStatus.CONFIRM.getCode());
        taskPickupDispatchDTO.setActualStartTime(LocalDateTime.now());
        pickupDispatchTaskFeign.updateById(id, taskPickupDispatchDTO);
        log.info("更新取派件任务 ID:{},PARAMS:{}", id, taskPickupDispatchDTO);

        //插入
        TransportOrderDTO transportOrderDTO = transportOrderFeign.findByOrderId(pickupDispatchDetailDTO.getOrderNumber());
        log.info("查询是否存在运单:{}", transportOrderDTO);
        if (transportOrderDTO == null || StringUtils.isBlank(transportOrderDTO.getId())) {
            TransportOrderDTO transportDTO = new TransportOrderDTO();
            transportDTO.setOrderId(pickupDispatchDetailDTO.getOrderNumber());
            transportDTO.setStatus(TransportOrderStatus.CREATED.getCode());
            transportDTO.setSchedulingStatus(TransportOrderSchedulingStatus.TO_BE_SCHEDULED.getCode());
            transportOrderFeign.save(transportDTO);
            log.info("不存在运单 创建新运单:{}", transportDTO);
        }

        return Result.ok();
    }


    @SneakyThrows
    @ApiOperation(value = "交件")
    @ApiImplicitParam(name = "tranOrderId", value = "运单号", required = true, example = "")
    @ResponseBody
    @PutMapping("warehousing/{tranOrderId}")
    public Result warehousing(@PathVariable("tranOrderId") String tranOrderId) {
        log.info(" 交件扫描运单号 ：{}", tranOrderId);

        TransportOrderDTO transportOrderDto = transportOrderFeign.findById(tranOrderId);
        if (ObjectUtils.isEmpty(transportOrderDto)) {
            return Result.error(400, "运单号未找到");
        }
        log.info(" 交件运单 ：{}", transportOrderDto);
        OrderDTO orderEditDTO = new OrderDTO();
        orderEditDTO.setStatus(OrderStatus.OUTLETS_WAREHOUSE.getCode());
        orderFeign.updateById(transportOrderDto.getOrderId(), orderEditDTO);


        TaskPickupDispatchDTO pickupDispatchTaskDto = pickupDispatchTaskFeign.findByOrderId(transportOrderDto.getOrderId(), PickupDispatchTaskType.PICKUP.getCode());
        TaskPickupDispatchDTO pickupDispatchTaskDtoUpdate = new TaskPickupDispatchDTO();
        pickupDispatchTaskDtoUpdate.setStatus(PickupDispatchTaskStatus.COMPLETED.getCode());
        pickupDispatchTaskDtoUpdate.setActualEndTime(LocalDateTime.now());
        pickupDispatchTaskDtoUpdate.setConfirmTime(LocalDateTime.now());
        pickupDispatchTaskFeign.updateById(pickupDispatchTaskDto.getId(), pickupDispatchTaskDtoUpdate);
        log.info("更新取派件任务 ID:{},PARAMS:{}", pickupDispatchTaskDto.getId(), pickupDispatchTaskDtoUpdate);

        return Result.ok();
    }

    @SneakyThrows
    @ApiOperation(value = "接件")
    @ApiImplicitParam(name = "tranOrderId", value = "运单号", required = true, example = "")
    @ResponseBody
    @PutMapping("handover/{tranOrderId}")
    public Result handover(@PathVariable("tranOrderId") String tranOrderId) {
        log.info("接件：{}", tranOrderId);
        // id 是运单号 扫描到的内容
        TransportOrderDTO transportOrderDto = transportOrderFeign.findById(tranOrderId);
        if (ObjectUtils.isEmpty(transportOrderDto)) {
            return Result.error(400, "运单号未找到");
        }
        String orderId = transportOrderDto.getOrderId();
        log.info("接件 获取运单信息：{} ,{}", tranOrderId, transportOrderDto);
        OrderDTO orderDto = orderFeign.findById(orderId);
        OrderDTO orderDTOUpdate = new OrderDTO();
        orderDTOUpdate.setStatus(OrderStatus.DISPATCHING.getCode());
        orderFeign.updateById(orderDto.getId(), orderDTOUpdate);
        log.info("接件 修改订单状态：{} ,{}", orderDto.getId(), orderDTOUpdate);
        TaskPickupDispatchDTO pickupDispatchTaskDto = pickupDispatchTaskFeign.findByOrderId(orderId, PickupDispatchTaskType.DISPATCH.getCode());
        TaskPickupDispatchDTO pickupDispatchTaskDtoUpdate = new TaskPickupDispatchDTO();
        pickupDispatchTaskDtoUpdate.setStatus(PickupDispatchTaskStatus.CONFIRM.getCode());
        pickupDispatchTaskDtoUpdate.setActualStartTime(LocalDateTime.now());
        pickupDispatchTaskFeign.updateById(pickupDispatchTaskDto.getId(), pickupDispatchTaskDtoUpdate);
        log.info("接件 修改派送任务状态：{} ,{}", pickupDispatchTaskDto.getId(), pickupDispatchTaskDtoUpdate);
        return Result.ok();
    }

    @SneakyThrows
    @ApiOperation(value = "妥投")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tranOrderId", value = "运单号", required = true, example = ""),
            @ApiImplicitParam(name = "status", value = "状态 1签收 0拒收", required = true, example = "")
    })
    @ResponseBody
    @PutMapping("delivered/{tranOrderId}/{status}")
    public Result delivered(@PathVariable("tranOrderId") String tranOrderId, @PathVariable("status") String status) {
        log.info("妥投 运单号：{} ，{}", tranOrderId, status);
        boolean state = "1".equals(status); // 1签收 0拒收
        // id 是运单号 扫描到的内容
        TransportOrderDTO transportOrderDto = transportOrderFeign.findById(tranOrderId);
        if (ObjectUtils.isEmpty(transportOrderDto)) {
            return Result.error(400, "运单号未找到");
        }
        TransportOrderDTO transportOrderDtoUpdate = new TransportOrderDTO();
        transportOrderDtoUpdate.setStatus(state ? TransportOrderStatus.RECEIVED.getCode() : TransportOrderStatus.REJECTED.getCode());
        transportOrderFeign.updateById(transportOrderDto.getId(), transportOrderDtoUpdate);
        log.info("妥投 获取运单信息：{} ,{}", transportOrderDto.getId(), transportOrderDtoUpdate);
        String orderId = transportOrderDto.getOrderId();
        OrderDTO orderDto = orderFeign.findById(orderId);
        OrderDTO orderDTOUpdate = new OrderDTO();
        orderDTOUpdate.setStatus(state ? OrderStatus.RECEIVED.getCode() : OrderStatus.REJECTION.getCode());
        orderFeign.updateById(orderDto.getId(), orderDTOUpdate);
        log.info("妥投 修改订单状态：{} ,{}", orderDto.getId(), orderDTOUpdate);
        TaskPickupDispatchDTO pickupDispatchTaskDto = pickupDispatchTaskFeign.findByOrderId(orderId, PickupDispatchTaskType.DISPATCH.getCode());
        TaskPickupDispatchDTO pickupDispatchTaskDtoUpdate = new TaskPickupDispatchDTO();
        pickupDispatchTaskDtoUpdate.setStatus(PickupDispatchTaskStatus.COMPLETED.getCode());
        pickupDispatchTaskDtoUpdate.setSignStatus(state ? PickupDispatchTaskSignStatus.RECEIVED.getCode() : PickupDispatchTaskSignStatus.REJECTION.getCode());
        pickupDispatchTaskDtoUpdate.setActualEndTime(LocalDateTime.now());
        pickupDispatchTaskDtoUpdate.setConfirmTime(LocalDateTime.now());
        pickupDispatchTaskFeign.updateById(pickupDispatchTaskDto.getId(), pickupDispatchTaskDtoUpdate);
        log.info("妥投 修改派送任务状态：{} ,{}", pickupDispatchTaskDto.getId(), pickupDispatchTaskDtoUpdate);
        return Result.ok();
    }

    @SneakyThrows
    @ApiOperation(value = "验证身份证号是否合法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNumber", value = "订单号", required = true, example = ""),
            @ApiImplicitParam(name = "code", value = "身份证号", required = true, example = "")
    })
    @ResponseBody
    @GetMapping("verifyIdCard")
    public Result verifyIdCard(@RequestParam String orderNumber, @RequestParam String code) {
        log.info("身份证号验证OrderId：{} Code:{} ", orderNumber, code);
        if (code.length() > 18 || code.length() < 15) {
            return Result.error(400, "身份证号不符合要求");
        }
        String regex = "\\d{15}(\\d{2}[0-9xX])?";

        if (!code.matches(regex)) {
            return Result.error(400, "身份证号不符合要求");
        }

        OrderDTO order = orderFeign.findById(orderNumber);
        String memberId = order.getMemberId();
        log.info("身份证号验证MemberId：{} ", memberId);
        Member member = new Member();
        member.setId(memberId);
        member.setIdCardNo(code);
        member.setIdCardNoVerify(MemberIdCardVerifyStatus.NONE.getCode());
        memberFeign.update(memberId, member);
        log.info("更新身份证号：{}", member);
        String checkResult = IdCardUtils.IdentityCardVerification(code);
        if (StringUtils.isNotBlank(checkResult)) {
            member.setIdCardNoVerify(MemberIdCardVerifyStatus.FAIL.getCode());
            memberFeign.update(memberId, member);
            log.info("更新身份证号 FAIL：{}", member);
            return Result.error(400, checkResult);
        }
        // 验证通过 写入客户端
        member.setIdCardNoVerify(MemberIdCardVerifyStatus.SUCCESS.getCode());
        memberFeign.update(memberId, member);
        log.info("更新身份证号 SUCCESS：{}", member);
        return Result.ok();
    }


    @SneakyThrows
    @ApiOperation(value = "路由")
    @ApiImplicitParam(name = "id", value = "主键", required = true, example = "")
    @ResponseBody
    @GetMapping("route")
    public Result route(String id) {
        log.info("路由信息 ID：{}", id);
        try {
            TaskPickupDispatchDTO pickupDispatchTaskDTO = pickupDispatchTaskFeign.findById(id);
            log.info("路由信息 TaskPickupDispatchDTO：{}", pickupDispatchTaskDTO);
            String orderId = pickupDispatchTaskDTO.getOrderId();

            TransportOrderDTO transportOrderDTO = transportOrderFeign.findByOrderId(orderId);
            log.info("路由信息 TransportOrderDTO：{}", pickupDispatchTaskDTO);

            List<TaskTransportDTO> transportTaskDTOs = transportTaskFeign.findAllByOrderIdOrTaskId(transportOrderDTO.getId(), null);

            Set<String> agencySet = new HashSet<>();
            agencySet.addAll(transportTaskDTOs.stream().map(item -> item.getStartAgencyId()).collect(Collectors.toSet()));
            agencySet.addAll(transportTaskDTOs.stream().map(item -> item.getEndAgencyId()).collect(Collectors.toSet()));

            CompletableFuture<Map<Long, Org>> orgMapFeture = PdCompletableFuture.agencyMapFuture(orgApi, null, agencySet, null);
            Map<Long, Org> orgMap = orgMapFeture.get();
            log.info("路由信息 AgencyMapFuture：{}", orgMap);

            List<RouteDTO> routeArray = new ArrayList<>();

            transportTaskDTOs.stream().forEach(item -> {
                if (null != item.getActualPickUpGoodsTime()) {
                    routeArray.add(RouteDTO.builder()
                            .arrivalTime(item.getActualPickUpGoodsTime())
                            .agencyName("快递在【" + orgMap.get(Long.valueOf(item.getStartAgencyId())).getName() + "】已装车，准备发往下一站")
                            .build());
                }
                if (null != item.getActualArrivalTime()) {
                    routeArray.add(RouteDTO.builder()
                            .arrivalTime(item.getActualArrivalTime())
                            .agencyName("快递已到达【" + orgMap.get(Long.valueOf(item.getEndAgencyId())).getName() + "】")
                            .build());
                }
            });

            Collections.reverse(routeArray);
            routeArray.forEach(item -> {
                log.info("路由信息：{}", item);
            });
            return Result.ok().put("data", routeArray);
        } catch (Exception e) {
            log.warn("路由查询异常", e);
            return Result.ok().put("data", new ArrayList<>());
        }
    }


    private OrderDTO buildOrderAndPrice(MailingSaveDTO entity) {

        OrderDTO orderAddDto = orderFeign.findById(entity.getOrderNumber());

        orderAddDto.setPaymentMethod(entity.getPayMethod());
        orderAddDto.setPaymentStatus(1); // 默认未付款

        orderAddDto.setOrderType(orderAddDto.getReceiverCityId().equals(orderAddDto.getSenderCityId()) ? OrderType.INCITY.getCode() : OrderType.OUTCITY.getCode());
        //TODO 计算总价 通过距离等信息 diesel
//        orderAddDto.setAmount(new BigDecimal("23"));
        OrderCargoDto cargoDto = buildOrderCargo(entity);
        orderAddDto.setOrderCargoDto(cargoDto);
        Map map = orderFeign.getOrderMsg(orderAddDto);
        orderAddDto.setAmount(new BigDecimal(map.getOrDefault("amount", "20").toString()));
        return orderAddDto;
    }

    private OrderCargoDto buildOrderCargo(MailingSaveDTO entity) {
        OrderCargoDto cargoDto = new OrderCargoDto();
        cargoDto.setName(entity.getGoodsName());
        cargoDto.setGoodsTypeId(entity.getGoodsType());
        cargoDto.setWeight(new BigDecimal(entity.getGoodsWeight()));
        cargoDto.setQuantity(1);
        cargoDto.setTotalWeight(cargoDto.getWeight().multiply(new BigDecimal(cargoDto.getQuantity())));
        return cargoDto;
    }

    /**
     * 预估总价
     *
     * @param entity
     * @return
     */
    @ApiOperation(value = "预估总价")
    @PostMapping("totalPrice")
    @ResponseBody
    public Result totalPrice(@RequestBody MailingSaveDTO entity) {
        log.info("计算预估总价：{}", entity);
        OrderDTO orderAddDto = buildOrderAndPrice(entity);
        return Result.ok().put("amount", orderAddDto.getAmount());
    }

}
