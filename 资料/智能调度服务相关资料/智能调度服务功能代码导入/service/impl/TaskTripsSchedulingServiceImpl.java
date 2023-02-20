package com.itheima.pinda.service.impl;

import com.itheima.pinda.DTO.OrderClassifyGroupDTO;
import com.itheima.pinda.DTO.OrderLineSimpleDTO;
import com.itheima.pinda.DTO.OrderLineTripsTruckDriverDTO;
import com.itheima.pinda.DTO.TripsTruckDriverDTO;
import com.itheima.pinda.DTO.transportline.TransportTripsDto;
import com.itheima.pinda.DTO.transportline.TransportTripsTruckDriverDto;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.DateUtils;
import com.itheima.pinda.entity.CacheLineDetailEntity;
import com.itheima.pinda.entity.OrderClassifyAttachEntity;
import com.itheima.pinda.feign.transportline.TransportTripsFeign;
import com.itheima.pinda.feign.truck.TruckFeign;
import com.itheima.pinda.service.IOrderClassifyAttachService;
import com.itheima.pinda.service.ITaskTripsSchedulingService;
import com.itheima.pinda.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 查找车次 车辆 司机
 *
 * @author wangsongyan
 */
@Service
@Slf4j
public class TaskTripsSchedulingServiceImpl implements ITaskTripsSchedulingService {

    @Autowired
    private TransportTripsFeign transportTripsFeign;

    @Autowired
    private TruckFeign truckFeign;

    @Autowired
    private UserApi userApi;

    @Autowired
    private IOrderClassifyAttachService orderClassifyAttachService;

    @Override
    public List<OrderLineTripsTruckDriverDTO> execute(List<OrderLineSimpleDTO> orderLineSimpleDTOS, String businessId, String jobId, String logId, String agencyId) {
        log.info("车次 车辆 司机 规划 {}", orderLineSimpleDTOS);
        List<OrderLineTripsTruckDriverDTO> orderLineTripsTruckDriverDTOS = new ArrayList<>();
        orderLineSimpleDTOS.forEach(orderLineSimpleDTO -> {
            CacheLineDetailEntity cacheLineDetailEntity = orderLineSimpleDTO.getCacheLineDetailEntity();


            // 根据线路获取车次
            List<TransportTripsDto> transportTripsDtos = transportTripsFeign.findAll(cacheLineDetailEntity.getTransportLineId(), null);
            if (CollectionUtils.isEmpty(transportTripsDtos)) {
                return;
            }
            log.info("根据线路获取车次:{}", transportTripsDtos);

            // 获取车次关联信息
            List<TransportTripsTruckDriverDto> transportTripsTruckDriverDtos = transportTripsFeign.findAllTruckDriverTransportTrips(transportTripsDtos.get(0).getId(), null, null);
            if (CollectionUtils.isEmpty(transportTripsTruckDriverDtos)) {
                return;
            }
            log.info("获取车次关联信息:{}", transportTripsTruckDriverDtos);

            // 可用车次列表 遍历集合  一个车次会有多个车和司机，获取状态正常的车次
            List<TripsTruckDriverDTO> tripsTruckDriverDTOS = getTripsTruckDriver(transportTripsTruckDriverDtos);
            if (CollectionUtils.isEmpty(tripsTruckDriverDTOS)) {
                return;
            }
            log.info("可用车次列表:{}", tripsTruckDriverDTOS);

            // 便利全部可用车次 根据时间匹配车次 （根据车次发车时间（小时：分钟）与当前时间比较，最近发车的车次作为最优方案）
            TripsTruckDriverDTO tripsTruckDriverDTO = getBestTripsTruckDriverDTO(tripsTruckDriverDTOS);
            if (tripsTruckDriverDTO == null) {
                return;
            }
            log.info("最优车次:{}", tripsTruckDriverDTO);

            //记录 订单分类id 关联车次 车辆 司机
            relation(tripsTruckDriverDTOS.get(0), orderLineSimpleDTO.getOrderClassifyGroupDTOS());
            log.info("记录车次信息");

            orderLineTripsTruckDriverDTOS.add(new OrderLineTripsTruckDriverDTO(tripsTruckDriverDTOS.get(0), cacheLineDetailEntity.getTransportLineId(), orderLineSimpleDTO.getOrderClassifyGroupDTOS()));


        });
        return orderLineTripsTruckDriverDTOS;
    }

    /**
     * 记录 订单分类id 关联车次 车辆 司机
     *
     * @param tripsTruckDriverDTO
     * @param orderClassifyGroupDTOS
     */
    private void relation(TripsTruckDriverDTO tripsTruckDriverDTO, List<OrderClassifyGroupDTO> orderClassifyGroupDTOS) {
        orderClassifyGroupDTOS.forEach(orderClassifyGroupDTO -> {
            OrderClassifyAttachEntity orderClassifyAttachEntity = new OrderClassifyAttachEntity();
            orderClassifyAttachEntity.setOrderClassifyId(orderClassifyGroupDTO.getId());
            orderClassifyAttachEntity.setTripsId(tripsTruckDriverDTO.getTripsId());
            orderClassifyAttachEntity.setTruckId(tripsTruckDriverDTO.getTruckId());
            orderClassifyAttachEntity.setDriverId(tripsTruckDriverDTO.getDriverId());
            orderClassifyAttachEntity.setId(IdUtils.get());
            orderClassifyAttachService.save(orderClassifyAttachEntity);
        });

    }

    /**
     * 获取最优方案
     *
     * @param tripsTruckDriverDTOS
     * @return
     */
    private TripsTruckDriverDTO getBestTripsTruckDriverDTO(List<TripsTruckDriverDTO> tripsTruckDriverDTOS) {
        List<String> tripsIds = tripsTruckDriverDTOS.stream().map(item -> item.getTripsId()).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(tripsIds)) {
            log.warn("关联车次信息未找到！");
            return null;
        }

        List<TransportTripsDto> transportTripsDtos = transportTripsFeign.findAll(null, tripsIds);

        LocalDateTime nowDate = LocalDateTime.now();

        TripsTruckDriverDTO tripsTruckDriverDTO = null;
        // 当天 发车时间最近的优先
        log.info("开始查找最优方案 共：{}条", transportTripsDtos.size());
        for (TransportTripsDto item : transportTripsDtos.stream().sorted(Comparator.comparing(TransportTripsDto::getDepartureTime)).collect(Collectors.toList())) {
            String[] departures = item.getDepartureTime().split(":");

            LocalTime departureTime = LocalTime.of(Integer.parseInt(departures[0]), Integer.parseInt(departures[1]));

            LocalDateTime departureDate = LocalDateTime.of(LocalDate.now(), departureTime);

            Duration duration = Duration.between(nowDate, DateUtils.getUTCTime(departureDate));

            long minute = duration.toMinutes();

            if (minute > 0) {
                tripsTruckDriverDTO = tripsTruckDriverDTOS.stream().collect(Collectors.toMap(TripsTruckDriverDTO::getTripsId, Function.identity(), (dto1, dto2) -> dto1)).get(item.getId());
                log.info("已找到最优方案");
                break;
            }
        }
        if (tripsTruckDriverDTO == null) {
            tripsTruckDriverDTO = tripsTruckDriverDTOS.get(0);
            log.info("未找到最优方案，使用第一种方案");
        }
        return tripsTruckDriverDTO;
    }

    /**
     * 获取全部可以用车次 车辆 司机
     *
     * @param transportTripsTruckDriverDtos
     * @return
     */
    private List<TripsTruckDriverDTO> getTripsTruckDriver(List<TransportTripsTruckDriverDto> transportTripsTruckDriverDtos) {

        Map<String, List<TransportTripsTruckDriverDto>> transportTripsTruckDriverDtoMap = transportTripsTruckDriverDtos.stream().collect(Collectors.groupingBy(TransportTripsTruckDriverDto::getTransportTripsId));

        List<TripsTruckDriverDTO> tripsTruckDriverDTOS = new ArrayList<>();
        transportTripsTruckDriverDtoMap.forEach((tripsId, transportTripsTruckDriverDtoList) -> {

            // 获取全部车辆 判断是否有可用车辆
            List<TruckDto> truckDtos = truckFeign.findAll(transportTripsTruckDriverDtoList.stream().filter(item -> StringUtils.isNotBlank(item.getTruckId())).map(item -> item.getTruckId()).collect(Collectors.toList()), null);
            // 获取状态正常的车辆id
            List<String> truckIds = truckDtos.stream().filter(item -> item.getStatus() == 1).map(item -> item.getId()).collect(Collectors.toList());

            // 获取全部司机id
            R<List<User>> userR = userApi.list(transportTripsTruckDriverDtoList.stream().filter(item -> StringUtils.isNotBlank(item.getUserId())).map(item -> Long.parseLong(item.getUserId())).collect(Collectors.toList()), null, null, null);
            List<User> users = userR.getData();
            // 正常状态司机id
            List<String> userIds = users.stream().filter(item -> item.getStatus()).map(item -> item.getId().toString()).collect(Collectors.toList());

            transportTripsTruckDriverDtoList.forEach(item -> {
                if (userIds.contains(item.getUserId()) && truckIds.contains(item.getTruckId())) {
                    // 可用车次 车辆  司机  添加到模型中
                    TripsTruckDriverDTO tripsTruckDriverDTO = new TripsTruckDriverDTO(tripsId, item.getTruckId(), item.getUserId());
                    tripsTruckDriverDTOS.add(tripsTruckDriverDTO);
                }
            });
        });
        return tripsTruckDriverDTOS;
    }
}
