package com.itheima.pinda.future;

import com.itheima.pinda.DTO.OrderCargoDto;
import com.itheima.pinda.DTO.TaskPickupDispatchDTO;
import com.itheima.pinda.DTO.TransportOrderDTO;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.base.R;
import com.itheima.pinda.enums.pickuptask.PickupDispatchTaskStatus;
import com.itheima.pinda.feign.CargoFeign;
import com.itheima.pinda.feign.PickupDispatchTaskFeign;
import com.itheima.pinda.feign.TransportOrderFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public class PdCompletableFuture {


    public static final CompletableFuture<Map<Long, Area>> areaMapFuture(AreaApi api, Long parentId, Set<Long> areaSet) {
        R<List<Area>> result = api.findAll(parentId, new ArrayList<>(areaSet));
        return CompletableFuture.supplyAsync(() ->
                result.getData().stream().collect(Collectors.toMap(Area::getId, vo -> vo)));
    }

    /**
     * 获取机构数据列表
     *
     * @param api        数据接口
     * @param agencyType 机构类型
     * @param ids        机构id列表
     * @return 执行结果
     */
    public static final CompletableFuture<Map<Long, Org>> agencyMapFuture(OrgApi api, Integer agencyType, Set<String> ids, Long countyId) {
        return CompletableFuture.supplyAsync(() -> {
            R<List<Org>> result = api.list(agencyType, ids.stream().mapToLong(id -> Long.valueOf(id)).boxed().collect(Collectors.toList()), countyId, null, null);
            if (result.getIsSuccess()) {
                return result.getData().stream().collect(Collectors.toMap(Org::getId, org -> org));
            }
            return new HashMap<>();
        });
    }

    /**
     * 获取货物信息列表
     *
     * @param api
     * @param cargoSet
     * @return
     */
    public static CompletableFuture<Map<String, OrderCargoDto>> cargoMapFuture(CargoFeign api, Set<String> cargoSet) {
        return CompletableFuture.supplyAsync(() -> {
            if (CollectionUtils.isEmpty(cargoSet)) {
                return new HashMap<>();
            }
            List<OrderCargoDto> result = api.list(cargoSet.stream().collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(result)) {
                return result.stream().collect(Collectors.toMap(OrderCargoDto::getOrderId, cargoDto -> cargoDto));
            }
            return new HashMap<>();
        });
    }


    /**
     * @param api
     * @param taskPickupDispatchSet
     * @param taskType
     * @return
     */
    public static CompletableFuture<Map<String, TaskPickupDispatchDTO>> taskTranSportMapFuture(PickupDispatchTaskFeign api, Set<String> taskPickupDispatchSet, Integer taskType) {
        return CompletableFuture.supplyAsync(() -> {
            TaskPickupDispatchDTO queryDTO = new TaskPickupDispatchDTO();
            queryDTO.setTaskType(taskType);
            queryDTO.setOrderIds(taskPickupDispatchSet.stream().collect(Collectors.toList()));
            List<TaskPickupDispatchDTO> result = api.findAll(queryDTO);
            if (!CollectionUtils.isEmpty(result)) {
                log.info("TaskPickupDispatchDTO result：{}", result);
                result = result.stream().filter(item -> (!PickupDispatchTaskStatus.CANCELLED.getCode().equals(item.getStatus()))).collect(Collectors.toList());
                log.info("TaskPickupDispatchDTO result by duplicate：{}", result);
                return result.stream().collect(Collectors.toMap(TaskPickupDispatchDTO::getOrderId, item -> item, (v1, v2) -> v1));
            }
            return new HashMap<>();
        });
    }

    public static CompletableFuture<Map<String, TransportOrderDTO>> transportOrderMapFuture(TransportOrderFeign api, Set<String> taskPickupDispatchSet) {
        return CompletableFuture.supplyAsync(() -> {
            if (CollectionUtils.isEmpty(taskPickupDispatchSet)) {
                return new HashMap<>();
            }
            List<TransportOrderDTO> result = api.findByOrderIds(taskPickupDispatchSet.stream().collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(result)) {
                return result.stream().collect(Collectors.toMap(TransportOrderDTO::getOrderId, transportOrderDTO -> transportOrderDTO));
            }
            return new HashMap<>();
        });
    }

}
