package com.itheima.pinda.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itheima.pinda.DTO.*;
import com.itheima.pinda.DTO.transportline.TransportLineDto;
import com.itheima.pinda.DTO.transportline.TransportTripsDto;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.common.utils.PageResponse;
import com.itheima.pinda.entity.*;
import com.itheima.pinda.feign.transportline.TransportLineFeign;
import com.itheima.pinda.feign.transportline.TransportTripsFeign;
import com.itheima.pinda.feign.truck.TruckFeign;
import com.itheima.pinda.future.PdCompletableFuture;
import com.itheima.pinda.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 定时任务日志
 *
 * @author
 */
@Slf4j
@RestController
@RequestMapping("/scheduleLog")
@Api(tags = "定时任务日志")
public class ScheduleJobLogController {
    @Autowired
    private IScheduleJobLogService scheduleJobLogService;
    @Autowired
    private IScheduleJobService scheduleJobService;

    @Autowired
    private OrgApi orgApi;
    @Autowired
    private IOrderClassifyService orderClassifyService;
    @Autowired
    private IOrderClassifyAttachService orderClassifyAttachService;
    @Autowired
    private ICacheLineUseService cacheLineUseService;
    @Autowired
    private ICacheLineService cacheLineService;
    @Autowired
    private ICacheLineDetailService cacheLineDetailService;

    @Autowired
    private TransportLineFeign transportLineFeign;
    @Autowired
    private TransportTripsFeign transportTripsFeign;
    @Autowired
    private UserApi userApi;
    @Autowired
    private TruckFeign truckFeign;


    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobId", value = "jobId", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orgId", value = "orgId", paramType = "query", dataType = "String")
    })
    public PageResponse<ScheduleJobLogDTO> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (!params.containsKey("page")) {
            params.put("page", 1);
        }
        if (!params.containsKey("pageSize")) {
            params.put("pageSize", 10);
        }
        if (!params.containsKey("jobId")) {
            String orgId = params.get("orgId").toString();
            ScheduleJobDTO scheduleJobDto = scheduleJobService.getByOrgId(orgId);
            params.put("jobId", scheduleJobDto.getId());
        }

        IPage<ScheduleJobLogEntity> page = scheduleJobLogService.page(params);

        List<ScheduleJobLogDTO> newItems = page.getRecords().stream().map(item -> {
            ScheduleJobLogDTO scheduleJobLogDTO = new ScheduleJobLogDTO();
            BeanUtils.copyProperties(item, scheduleJobLogDTO);
            return scheduleJobLogDTO;
        }).collect(Collectors.toList());

        return PageResponse.<ScheduleJobLogDTO>builder()
                .page(Integer.valueOf(String.valueOf(page.getCurrent())))
                .pagesize(Integer.valueOf(String.valueOf(page.getSize())))
                .pages(page.getPages())
                .counts(page.getTotal())
                .items(newItems)
                .build();
    }

    @SneakyThrows
    @GetMapping("{id}")
    @ApiOperation("信息")
    public ScheduleJobLogDTO info(@PathVariable("id") Long id) {
        ScheduleJobLogDTO logDto = scheduleJobLogService.get(id);
        // 组合 订单分组等信息
        List<OrderClassifyEntity> orderClassifyEntities = orderClassifyService.findByJobLogId(logDto.getId());
        Set<String> agencySet = new HashSet<>();
        agencySet.addAll(orderClassifyEntities.stream().map(item -> item.getStartAgencyId()).collect(Collectors.toSet()));
        agencySet.addAll(orderClassifyEntities.stream().map(item -> item.getEndAgencyId()).collect(Collectors.toSet()));

        CompletableFuture<Map<Long, Org>> agnecyMapFuture = PdCompletableFuture.agencyMapFuture(orgApi, null, agencySet, null);
        Map<Long, Org> agencyMap = agnecyMapFuture.get();

        List<OrderClassifyLogDTO> orderClassifyLogDTOS = orderClassifyEntities.stream().map(item -> {
            OrderClassifyLogDTO orderClassifyLogDTO = new OrderClassifyLogDTO();
            BeanUtils.copyProperties(item, orderClassifyLogDTO);
            if (StringUtils.isNotEmpty(orderClassifyLogDTO.getStartAgencyId())) {
                orderClassifyLogDTO.setStartAgency(agencyMap.get(Long.parseLong(orderClassifyLogDTO.getStartAgencyId())).getName());
            }
            if (StringUtils.isNotBlank(orderClassifyLogDTO.getEndAgencyId())) {
                orderClassifyLogDTO.setEndAgency(agencyMap.get(Long.parseLong(orderClassifyLogDTO.getEndAgencyId())).getName());
            }

            buildTransportLine(item, orderClassifyLogDTO);

            buildTripsTruckDriver(item, orderClassifyLogDTO);

            return orderClassifyLogDTO;
        }).collect(Collectors.toList());

        logDto.setOrderClassifyLogDTOS(orderClassifyLogDTOS);
        return logDto;
    }

    /**
     * 获取当前使用的车次 车辆 司机信息
     *
     * @param item
     * @param orderClassifyLogDTO
     */
    @SneakyThrows
    private void buildTripsTruckDriver(OrderClassifyEntity item, OrderClassifyLogDTO orderClassifyLogDTO) {
        LambdaQueryWrapper<OrderClassifyAttachEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderClassifyAttachEntity::getOrderClassifyId, item.getId());
        wrapper.orderByAsc(OrderClassifyAttachEntity::getCreateDate);
        List<OrderClassifyAttachEntity> orderClassifyAttachs = orderClassifyAttachService.list(wrapper);

        Set<String> tripsIdSet = orderClassifyAttachs.stream().map(OrderClassifyAttachEntity::getTripsId).collect(Collectors.toSet());
        Set<String> truckIdSet = orderClassifyAttachs.stream().map(OrderClassifyAttachEntity::getTruckId).collect(Collectors.toSet());
        Set<String> driverIdSet = orderClassifyAttachs.stream().map(OrderClassifyAttachEntity::getDriverId).collect(Collectors.toSet());

        CompletableFuture<Map<String, TransportTripsDto>> tripsMapFuture = PdCompletableFuture.tripsMapFuture(transportTripsFeign, tripsIdSet);
        CompletableFuture<Map<String, TruckDto>> truckMapFuture = PdCompletableFuture.truckMapFuture(truckFeign, truckIdSet);
        CompletableFuture<Map<Long, User>> driverMapFuture = PdCompletableFuture.driverMapFuture(userApi, driverIdSet);

        Set<String> tripsSet = new LinkedHashSet<>();
        Set<String> truckSet = new LinkedHashSet<>();
        Set<String> driverSet = new LinkedHashSet<>();
        Map<String, TransportTripsDto> tripsMap = tripsMapFuture.get();
        Map<String, TruckDto> truckMap = truckMapFuture.get();
        Map<Long, User> driverMap = driverMapFuture.get();
        for (OrderClassifyAttachEntity orderClassifyAttach : orderClassifyAttachs) {
            tripsSet.add(tripsMap.get(orderClassifyAttach.getTripsId()).getName());
            truckSet.add(truckMap.get(orderClassifyAttach.getTruckId()).getLicensePlate());
            driverSet.add(driverMap.get(Long.parseLong(orderClassifyAttach.getDriverId())).getName());
        }
        orderClassifyLogDTO.setTripsSet(tripsSet);
        orderClassifyLogDTO.setTruckSet(truckSet);
        orderClassifyLogDTO.setDriverSet(driverSet);
    }

    /**
     * 构建当前分类的订单经过的全部机构 和 全部的线路
     *
     * @param item
     * @param orderClassifyLogDTO
     */
    @SneakyThrows
    private void buildTransportLine(OrderClassifyEntity item, OrderClassifyLogDTO orderClassifyLogDTO) {
        CacheLineUseEntity cacheLineUseEntity = cacheLineUseService.getByOrderClassifyId(item.getId());
        Set<String> lineSet = new LinkedHashSet<>();
        Set<String> transportLineSet = new LinkedHashSet<>();
        if (cacheLineUseEntity != null) {
            CacheLineEntity cacheLineEntity = cacheLineService.getById(cacheLineUseEntity.getCacheLineId());
            List<CacheLineDetailEntity> cacheLineDetailEntities = cacheLineDetailService.findByCacheLineId(cacheLineUseEntity.getCacheLineId());
            Set<String> transportLineIdSet = cacheLineDetailEntities.stream().map(cacheLineDetailEntity -> cacheLineDetailEntity.getTransportLineId()).collect(Collectors.toSet());
            CompletableFuture<Map<String, TransportLineDto>> transportLineMapFuture = PdCompletableFuture.transportLineIdMapFuture(transportLineFeign, transportLineIdSet);

            Set<String> agencySet = new HashSet<>();
            agencySet.add(cacheLineEntity.getStartAgencyId());
            agencySet.add(cacheLineEntity.getEndAgencyId());
            agencySet.addAll(cacheLineDetailEntities.stream().map(cacheLineDetailEntity -> cacheLineDetailEntity.getStartAgencyId()).collect(Collectors.toSet()));
            agencySet.addAll(cacheLineDetailEntities.stream().map(cacheLineDetailEntity -> cacheLineDetailEntity.getEndAgencyId()).collect(Collectors.toSet()));

            CompletableFuture<Map<Long, Org>> agnecyMapFu = PdCompletableFuture.agencyMapFuture(orgApi, null, agencySet, null);
            Map<String, TransportLineDto> transportLineMap = transportLineMapFuture.get();
            Map<Long, Org> agency = agnecyMapFu.get();

            cacheLineDetailEntities.forEach(cacheLineDetailEntity -> {
                CacheLineDetailDTO cacheLineDetailDTO = new CacheLineDetailDTO();
                BeanUtils.copyProperties(cacheLineDetailEntity, cacheLineDetailDTO);
                if (StringUtils.isNotEmpty(cacheLineDetailDTO.getStartAgencyId())) {
                    cacheLineDetailDTO.setStartAgency(agency.get(Long.parseLong(cacheLineDetailDTO.getStartAgencyId())).getName());
                }
                if (StringUtils.isNotBlank(cacheLineDetailDTO.getEndAgencyId())) {
                    cacheLineDetailDTO.setEndAgency(agency.get(Long.parseLong(cacheLineDetailDTO.getEndAgencyId())).getName());
                }
                transportLineSet.add(transportLineMap.get(cacheLineDetailDTO.getTransportLineId()).getName());

                lineSet.add(cacheLineDetailDTO.getStartAgency());
                lineSet.add(cacheLineDetailDTO.getEndAgency());

            });

            orderClassifyLogDTO.setTransportLineSet(transportLineSet);
            orderClassifyLogDTO.setLineSet(lineSet);


            CacheLineDTO cacheLineDTO = new CacheLineDTO();
            BeanUtils.copyProperties(cacheLineEntity, cacheLineDTO);
            orderClassifyLogDTO.setCacheLineDTO(cacheLineDTO);
        }
    }
}