package com.itheima.pinda.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.itheima.pinda.DTO.OrderClassifyDTO;
import com.itheima.pinda.DTO.OrderClassifyGroupDTO;
import com.itheima.pinda.DTO.OrderSearchDTO;
import com.itheima.pinda.DTO.angency.AgencyScopeDto;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.base.R;
import com.itheima.pinda.common.utils.EntCoordSyncJob;
import com.itheima.pinda.common.utils.Result;
import com.itheima.pinda.entity.Order;
import com.itheima.pinda.entity.OrderClassifyEntity;
import com.itheima.pinda.entity.OrderClassifyOrderEntity;
import com.itheima.pinda.enums.OrderStatus;
import com.itheima.pinda.feign.OrderFeign;
import com.itheima.pinda.feign.agency.AgencyScopeFeign;
import com.itheima.pinda.future.PdCompletableFuture;
import com.itheima.pinda.service.IOrderClassifyOrderService;
import com.itheima.pinda.service.IOrderClassifyService;
import com.itheima.pinda.service.ITaskOrderClassifyService;
import com.itheima.pinda.utils.IdUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 订单分类实现类
 */
@Service
@Slf4j
public class TaskOrderClassifyServiceImpl implements ITaskOrderClassifyService {
    @Autowired
    private OrderFeign orderFeign;
    @Autowired
    private AgencyScopeFeign agencyScopeFeign;
    @Autowired
    private IOrderClassifyService orderClassifyService;
    @Autowired
    private IOrderClassifyOrderService orderClassifyOrderService;

    /**
     * 订单分类核心逻辑
     * @param agencyId 机构id（网点或者转运中心的id）
     * @param jobId 定时任务id
     * @param logId 日志id
     * @return
     */
    @Override
    public List<OrderClassifyGroupDTO> execute(String agencyId, String jobId, String logId) {
        //用于存放当前机构下的订单
        List<OrderClassifyDTO> orderClassifyDTOS = new ArrayList<>();
        List<OrderClassifyGroupDTO> orderClassifyGroupDTOS = new ArrayList<>();

        //获取当前机构下所有的订单数据(分为新订单和中转订单）
        orderClassifyDTOS.addAll(buildNewOrder(agencyId));
        orderClassifyDTOS.addAll(buildTransferOrder(agencyId));

        //进行订单分类
        Map<String, List<OrderClassifyDTO>> orderClassifyDTOGroup = orderClassifyDTOS.stream().collect(Collectors.groupingBy(OrderClassifyDTO::groupBy));

        OrderClassifyGroupDTO.OrderClassifyGroupDTOBuilder builder = OrderClassifyGroupDTO.builder();

        //进行对象转换，将当前Map对象转为 List<OrderClassifyGroupDTO>类型
        orderClassifyDTOGroup.forEach((key,value) -> {
            builder.key(key);
            //获取原始订单对象
            List<Order> orders = value.stream().map((item) -> item.getOrder()).collect(Collectors.toList());
            builder.orders(orders);
            OrderClassifyGroupDTO orderClassifyGroupDTO = builder.build();
            orderClassifyGroupDTOS.add(orderClassifyGroupDTO);
        });

        //将分类结果保存到数据库
        saveRecord(orderClassifyGroupDTOS,jobId,logId);

        return orderClassifyGroupDTOS;
    }

    /**
     * 保存订单分类结果
     * @param orderClassifyGroupDTOS
     * @param jobId
     * @param logId
     */
    private void saveRecord(List<OrderClassifyGroupDTO> orderClassifyGroupDTOS, String jobId, String logId) {
        orderClassifyGroupDTOS.forEach(item -> {
            if (item.isNew()) {
                log.info("新订单 保存分组信息");
                OrderClassifyEntity entity = new OrderClassifyEntity();
                entity.setClassify(item.getKey());
                entity.setJobId(jobId);
                entity.setJobLogId(logId);
                if (!entity.getClassify().equals("ERROR")) {
                    entity.setStartAgencyId(item.getStartAgencyId());
                    entity.setEndAgencyId(item.getEndAgencyId());
                }
                entity.setTotal(item.getOrders().size());
                entity.setId(IdUtils.get());

                List<OrderClassifyOrderEntity> orderClassifyOrders = item.getOrders().stream().map((order) -> {
                    OrderClassifyOrderEntity orderClassifyOrderEntity = new OrderClassifyOrderEntity();
                    orderClassifyOrderEntity.setOrderId(order.getId());
                    orderClassifyOrderEntity.setOrderClassifyId(entity.getId());
                    orderClassifyOrderEntity.setId(IdUtils.get());
                    return orderClassifyOrderEntity;
                }).collect(Collectors.toList());

                item.setId(entity.getId());
                orderClassifyService.save(entity);
                orderClassifyOrderService.saveBatch(orderClassifyOrders);
            } else {
                log.info("中转订单，查询分组信息");
                List<String> orderIds = item.getOrders().stream().map(order -> order.getId()).collect(Collectors.toList());
                log.info("当前分组的订单id：{}", orderIds);
                LambdaQueryWrapper<OrderClassifyOrderEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(OrderClassifyOrderEntity::getOrderId, orderIds);
                List<OrderClassifyOrderEntity> orderClassifyOrders = orderClassifyOrderService.list(wrapper);
                // 不出意外只会查到一个订单分类id
                Set<String> orderClassifyIds = orderClassifyOrders.stream().map(orderClassifyOrderEntity -> orderClassifyOrderEntity.getOrderClassifyId()).collect(Collectors.toSet());
                log.info("查询订单分组id:{}", orderClassifyIds);
                if (CollectionUtils.isEmpty(orderClassifyIds)) {
                    log.error("中转订单异常:{}", orderIds);
                    return;
                }
                item.setId(orderClassifyIds.iterator().next());
            }
        });
    }

    /**
     *查询中转订单
     * @param agencyId
     * @return
     */
    private List<OrderClassifyDTO> buildTransferOrder(String agencyId) {
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        //订单状态为运输中
        orderSearchDTO.setStatus(OrderStatus.IN_TRANSIT.getCode());
        orderSearchDTO.setCurrentAgencyId(agencyId);
        //调用Feign接口实现远程调用，查询当前机构下的新订单
        List<Order> orderList = orderFeign.list(orderSearchDTO);

        log.info("查询到新订单个数：{}",orderList.size());

        OrderClassifyDTO.OrderClassifyDTOBuilder builder = OrderClassifyDTO.builder();
        builder.currentAgencyId(agencyId);

        List<OrderClassifyDTO> orderClassifyDTOList = orderList.stream().map((item) -> {
            builder.order(item);//原始订单
            builder.startAgencyId(getStartAgencyId(item));//起始机构
            builder.endAgencyId(getEndAgencyId(item));//目的地机构（目的地网点）
            builder.orderType(item.getOrderType());
            return builder.build();
        }).collect(Collectors.toList());

        return orderClassifyDTOList;
    }

    /**
     * 根据订单获得起始机构id
     * @param order
     * @return
     */
    private String getStartAgencyId(Order order) {
        //根据当前订单获取发件人地址详细信息，包含省市区，例如：北京市昌平区建材城西路金燕龙办公楼
        String address = senderFullAddress(order);
        if(StringUtils.isBlank(address)){
            exceptionHappend("收件人地址不能为空");
        }

        //调用百度地图工具类，根据地址获取对应的经纬度坐标
        String location = EntCoordSyncJob.getCoordinate(address);
        if(StringUtils.isBlank(location)){
            exceptionHappend("收件人地址不正确");
        }

        log.info("根据地址{}获取对应的坐标值{}",address,location);

        //根据经纬度坐标获取对应的区县检查通过百度地图获取的区域是否和下单时选择的区域一致
        Map map = EntCoordSyncJob.getLocationByPosition(location);//根据经纬度获取对应的区域信息（从百度官方获取）
        if(ObjectUtils.isEmpty(map)){
            exceptionHappend("根据经纬度获取区域信息为空");
        }

        String adcode = (String) map.get("adcode");

        //根据adcode（区域编码）查询我们系统中的区域信息
        R<Area> areaR = areaApi.getByCode(adcode + "000000");
        Area area = areaR.getData();
        if(area == null){
            exceptionHappend("没有查询到区域数据");
        }

        if(!order.getSenderCountyId().equals(area.getId())){
            exceptionHappend("发件地址区域id和根据坐标计算出的区域不一致");
        }

        //查询当前区县下的所有网点
        List<AgencyScopeDto> agencyScope = agencyScopeFeign.findAllAgencyScope(area.getId().toString(), null, null, null);
        if(agencyScope == null || agencyScope.size() == 0){
            exceptionHappend("无法获取收件人所在网点信息");
        }

        //计算当前区域下所有网点距离收件人地址最近的网点
        Result result = caculate(agencyScope,location);

        return (String) result.get("agencyId");
    }

    /**
     * 获得发件人详细地址信息
     * @param order
     * @return
     */
    @SneakyThrows
    private String senderFullAddress(Order order) {
        Long provinceId = Long.valueOf(order.getSenderProvinceId());//省份id
        Long cityId = Long.valueOf(order.getSenderCityId());//市id
        Long countyId = Long.valueOf(order.getSenderCountyId());//区id

        Set<Long> areaSet = new HashSet<>();
        areaSet.add(provinceId);
        areaSet.add(cityId);
        areaSet.add(countyId);

        CompletableFuture<Map<Long, Area>> future = PdCompletableFuture.areaMapFuture(areaApi, null, areaSet);
        Map<Long, Area> areaMap = future.get();

        //根据key（id的值）获取到对应的区域Area对象
        String provinceName = areaMap.get(provinceId).getName();
        String cityName = areaMap.get(cityId).getName();
        String countyName = areaMap.get(countyId).getName();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(provinceName).append(cityName).append(countyName).append(order.getSenderAddress());

        return stringBuffer.toString();
    }

    /**
     * 获取指定机构下的新订单
     * @param agencyId
     * @return
     */
    private List<OrderClassifyDTO> buildNewOrder(String agencyId) {
        OrderSearchDTO orderSearchDTO = new OrderSearchDTO();
        //订单状态为网点入库
        orderSearchDTO.setStatus(OrderStatus.OUTLETS_WAREHOUSE.getCode());
        orderSearchDTO.setCurrentAgencyId(agencyId);
        //调用Feign接口实现远程调用，查询当前机构下的新订单
        List<Order> orderList = orderFeign.list(orderSearchDTO);

        log.info("查询到新订单个数：{}",orderList.size());

        OrderClassifyDTO.OrderClassifyDTOBuilder builder = OrderClassifyDTO.builder();
        builder.currentAgencyId(agencyId);

        List<OrderClassifyDTO> orderClassifyDTOList = orderList.stream().map((item) -> {
            builder.order(item);//原始订单
            builder.startAgencyId(agencyId);//起始机构
            builder.endAgencyId(getEndAgencyId(item));//目的地机构（目的地网点）
            builder.orderType(item.getOrderType());
            return builder.build();
        }).collect(Collectors.toList());

        return orderClassifyDTOList;
    }

    /**
     * 抛出异常
     * @param msg
     */
    private void exceptionHappend(String msg){
        try {
            throw new Exception(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取目的地网点id
     * @param order
     * @return
     */
    private String getEndAgencyId(Order order) {
        //根据当前订单获取收件人地址详细信息，包含省市区，例如：北京市昌平区建材城西路金燕龙办公楼
        String address = receiverFullAddress(order);
        if(StringUtils.isBlank(address)){
            exceptionHappend("收件人地址不能为空");
        }

        //调用百度地图工具类，根据地址获取对应的经纬度坐标
        String location = EntCoordSyncJob.getCoordinate(address);
        if(StringUtils.isBlank(location)){
            exceptionHappend("收件人地址不正确");
        }

        log.info("根据地址{}获取对应的坐标值{}",address,location);

        //根据经纬度坐标获取对应的区县检查通过百度地图获取的区域是否和下单时选择的区域一致
        Map map = EntCoordSyncJob.getLocationByPosition(location);//根据经纬度获取对应的区域信息（从百度官方获取）
        if(ObjectUtils.isEmpty(map)){
            exceptionHappend("根据经纬度获取区域信息为空");
        }

        String adcode = (String) map.get("adcode");

        //根据adcode（区域编码）查询我们系统中的区域信息
        R<Area> areaR = areaApi.getByCode(adcode + "000000");
        Area area = areaR.getData();
        if(area == null){
            exceptionHappend("没有查询到区域数据");
        }

        if(!order.getReceiverCountyId().equals(area.getId())){
            exceptionHappend("收货地址区域id和根据坐标计算出的区域不一致");
        }

        //查询当前区县下的所有网点
        List<AgencyScopeDto> agencyScope = agencyScopeFeign.findAllAgencyScope(area.getId().toString(), null, null, null);
        if(agencyScope == null || agencyScope.size() == 0){
            exceptionHappend("无法获取收件人所在网点信息");
        }

        //计算当前区域下所有网点距离收件人地址最近的网点
        Result result = caculate(agencyScope,location);

        return (String) result.get("agencyId");
    }

    /**
     * 从给定网点中查找覆盖指定点的网点
     * @param agencyScopes
     * @param location
     * @return
     */
    private Result caculate(List<AgencyScopeDto> agencyScopes,String location) {
        try {
            for (AgencyScopeDto agencyScopeDto : agencyScopes) {
                List<List<Map>> mutiPoints = agencyScopeDto.getMutiPoints();
                for (List<Map> list : mutiPoints) {
                    String[] originArray = location.split(",");
                    //判断某个点是否在指定区域范围内
                    boolean flag = EntCoordSyncJob.isInScope(list, Double.parseDouble(originArray[0]), Double.parseDouble(originArray[1]));
                    if (flag) {
                        return Result.ok().put("agencyId", agencyScopeDto.getAgencyId());
                    }
                }
            }
            return Result.error(5000, "获取网点失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(5000, "获取网点失败");
        }
    }

    /**
     * 获取坐标值
     * @param map
     * @return
     */
    private String getPoint(Map map){
        String lng = map.get("lng").toString();
        String lat = map.get("lat").toString();
        return lng + "," + lat;
    }

    public static void main(String[] args) {
        /*String address = "北京市昌平区建材城西路金燕龙办公楼";
        String location = EntCoordSyncJob.getCoordinate(address);

        //调用百度地图，根据经纬度获取区域信息
        Map map = EntCoordSyncJob.getLocationByPosition(location);

        System.out.println(location);
        System.out.println("----");
        System.out.println(map);

        String adcode = (String) map.get("adcode");
        System.out.println(adcode);*/

        /*List<AgencyScopeDto> agencyScopeList = new ArrayList<>();
        AgencyScopeDto dto1 = new AgencyScopeDto();
        dto1.setAgencyId("1");
        List<List<Map>> points1 = new ArrayList<>();
        List<Map> points1_1 = new ArrayList<>();
        Map point1 = new HashMap();
        point1.put("lng",116.337566);
        point1.put("lat",40.067944);
        Map point2 = new HashMap();
        point2.put("lng",116.362215);
        point2.put("lat",40.0741);
        points1_1.add(point1);
        points1_1.add(point2);
        points1.add(points1_1);
        dto1.setMutiPoints(points1);

        AgencyScopeDto dto2 = new AgencyScopeDto();
        dto2.setAgencyId("2");
        List<List<Map>> points2 = new ArrayList<>();
        List<Map> points2_1 = new ArrayList<>();
        Map point3 = new HashMap();
        point3.put("lng",116.3344);
        point3.put("lat",40.067);
        Map point4 = new HashMap();
        point4.put("lng",116.311215);
        point4.put("lat",40.10741);
        points2_1.add(point3);
        points2_1.add(point4);
        points2.add(points2_1);
        dto2.setMutiPoints(points2);


        agencyScopeList.add(dto1);
        agencyScopeList.add(dto2);
        String location = "116.349936,40.066258";
        new TaskOrderClassifyServiceImpl().caculate(agencyScopeList,location);*/


        List<OrderClassifyDTO> orderClassifyDTOS = new ArrayList<>();
        List<OrderClassifyGroupDTO> orderClassifyGroupDTOS = new ArrayList<>();

        OrderClassifyDTO.OrderClassifyDTOBuilder builder1 = OrderClassifyDTO.builder();
        builder1.startAgencyId("1");
        builder1.endAgencyId("10");
        builder1.currentAgencyId("1");
        Order order = new Order();
        order.setId("10001");
        builder1.order(order);
        OrderClassifyDTO dto1 = builder1.build();
        orderClassifyDTOS.add(dto1);

        OrderClassifyDTO.OrderClassifyDTOBuilder builder2 = OrderClassifyDTO.builder();
        builder2.startAgencyId("1");
        builder2.endAgencyId("10");
        builder2.currentAgencyId("1");
        Order order2 = new Order();
        order2.setId("10002");
        builder2.order(order2);
        OrderClassifyDTO dto2 = builder2.build();
        orderClassifyDTOS.add(dto2);

        OrderClassifyDTO.OrderClassifyDTOBuilder builder3 = OrderClassifyDTO.builder();
        builder3.startAgencyId("2");
        builder3.endAgencyId("12");
        builder3.currentAgencyId("11");
        Order order3 = new Order();
        order3.setId("10003");
        builder3.order(order3);
        OrderClassifyDTO dto3 = builder3.build();
        orderClassifyDTOS.add(dto3);

        Map<String, List<OrderClassifyDTO>> orderClassifyDTOGroup = orderClassifyDTOS.stream().collect(Collectors.groupingBy(OrderClassifyDTO::groupBy));

        OrderClassifyGroupDTO.OrderClassifyGroupDTOBuilder builder = OrderClassifyGroupDTO.builder();

        //进行对象转换，将当前Map对象转为 List<OrderClassifyGroupDTO>类型
        orderClassifyDTOGroup.forEach((key,value) -> {
            builder.key(key);
            //获取原始订单对象
            List<Order> orders = value.stream().map((item) -> item.getOrder()).collect(Collectors.toList());
            builder.orders(orders);
            OrderClassifyGroupDTO orderClassifyGroupDTO = builder.build();
            orderClassifyGroupDTOS.add(orderClassifyGroupDTO);
        });

        System.out.println(orderClassifyGroupDTOS);
    }

    @Autowired
    private AreaApi areaApi;

    /**
     * 根据订单获取对应的完整收件人地址信息
     * @param order
     * @return
     */
    @SneakyThrows
    private String receiverFullAddress(Order order) {
        Long provinceId = Long.valueOf(order.getReceiverProvinceId());//省份id
        Long cityId = Long.valueOf(order.getReceiverCityId());//市id
        Long countyId = Long.valueOf(order.getReceiverCountyId());//区id

        Set<Long> areaSet = new HashSet<>();
        areaSet.add(provinceId);
        areaSet.add(cityId);
        areaSet.add(countyId);

        CompletableFuture<Map<Long, Area>> future = PdCompletableFuture.areaMapFuture(areaApi, null, areaSet);
        Map<Long, Area> areaMap = future.get();

        //根据key（id的值）获取到对应的区域Area对象
        String provinceName = areaMap.get(provinceId).getName();
        String cityName = areaMap.get(cityId).getName();
        String countyName = areaMap.get(countyId).getName();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(provinceName).append(cityName).append(countyName).append(order.getReceiverAddress());

        return stringBuffer.toString();
    }


}
