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
        return null;
    }

    /**
     * 保存订单分类结果
     * @param orderClassifyGroupDTOS
     * @param jobId
     * @param logId
     */
    private void saveRecord(List<OrderClassifyGroupDTO> orderClassifyGroupDTOS, String jobId, String logId) {

    }

    /**
     *查询中转订单
     * @param agencyId
     * @return
     */
    private List<OrderClassifyDTO> buildTransferOrder(String agencyId) {
        return null;
    }

    /**
     * 根据订单获得起始机构id
     * @param order
     * @return
     */
    private String getStartAgencyId(Order order) {
        return null;
    }

    /**
     * 获得发件人详细地址信息
     * @param order
     * @return
     */
    @SneakyThrows
    private String senderFullAddress(Order order) {
        return null;
    }

    /**
     * 获取指定机构下的新订单
     * @param agencyId
     * @return
     */
    private List<OrderClassifyDTO> buildNewOrder(String agencyId) {
        return null;
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
        return null;
    }

    /**
     * 从给定网点中查找覆盖指定点的网点
     * @param agencyScopes
     * @param location
     * @return
     */
    private Result caculate(List<AgencyScopeDto> agencyScopes,String location) {
        return null;
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
        return null;
    }
}
