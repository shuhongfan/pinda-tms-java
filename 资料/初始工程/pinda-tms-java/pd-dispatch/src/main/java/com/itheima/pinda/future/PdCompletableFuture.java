package com.itheima.pinda.future;

import com.itheima.pinda.DTO.OrderCargoDto;
import com.itheima.pinda.DTO.angency.AgencyScopeDto;
import com.itheima.pinda.DTO.transportline.TransportLineDto;
import com.itheima.pinda.DTO.transportline.TransportTripsDto;
import com.itheima.pinda.DTO.truck.TruckDto;
import com.itheima.pinda.authority.api.AreaApi;
import com.itheima.pinda.authority.api.OrgApi;
import com.itheima.pinda.authority.api.UserApi;
import com.itheima.pinda.authority.entity.auth.User;
import com.itheima.pinda.authority.entity.common.Area;
import com.itheima.pinda.authority.entity.core.Org;
import com.itheima.pinda.authority.enumeration.core.OrgType;
import com.itheima.pinda.base.R;
import com.itheima.pinda.feign.CargoFeign;
import com.itheima.pinda.feign.agency.AgencyScopeFeign;
import com.itheima.pinda.feign.transportline.TransportLineFeign;
import com.itheima.pinda.feign.transportline.TransportTripsFeign;
import com.itheima.pinda.feign.truck.TruckFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
public class PdCompletableFuture {
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
            log.info("agencyMapFuture : {} , {} , {}", agencyType, ids, countyId);
            List<Long> idList = ids.stream().filter(item -> StringUtils.isNotBlank(item)).mapToLong(id -> Long.valueOf(id)).boxed().collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(idList)) {
                R<List<Org>> result = api.list(agencyType,
                        idList,
                        countyId,
                        null,
                        new ArrayList<>());
                if (result.getIsSuccess()) {
                    return result.getData().stream().collect(Collectors.toMap(Org::getId, org -> org));
                }
            }
            return new HashMap<>();
        });
    }

    public static CompletableFuture<Map<Long, Org>> businessHallMapFuture(OrgApi feign, Set<String> set) {
        return CompletableFuture.supplyAsync(() -> {
            List<Long> list = set.stream().map(item -> Long.parseLong(item)).collect(Collectors.toList());
            R<List<Org>> orgRs = feign.listByCountyIds(OrgType.BUSINESS_HALL.getType(), list);
            List<Org> orgs = orgRs.getData();
            return orgs.stream().collect(Collectors.toMap(Org::getCountyId, value -> value));
        });
    }

    public static CompletableFuture<Map<String, String>> agencyScopeMapFuture(AgencyScopeFeign feign, Set<String> set) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> list = set.stream().collect(Collectors.toList());
            List<AgencyScopeDto> agencyScopeDtos = feign.findAllAgencyScope(null, null, null, list);
            return agencyScopeDtos.stream().collect(Collectors.toMap(AgencyScopeDto::getAreaId, value -> value.getAgencyId()));
        });
    }

    public static CompletableFuture<Map<String, OrderCargoDto>> orderCargoMapFuture(CargoFeign feign, Set<String> set) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> list = set.stream().collect(Collectors.toList());
            List<OrderCargoDto> orderCargoDtos = feign.list(list);
            return orderCargoDtos.stream().collect(Collectors.toMap(OrderCargoDto::getOrderId, value -> value));
        });
    }

    public static CompletableFuture<Map<String, TransportLineDto>> transportLineIdMapFuture(TransportLineFeign api, Set<String> transportLineIdSet) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> list = transportLineIdSet.stream().collect(Collectors.toList());
            List<TransportLineDto> result = api.findAll(list, null, null);
            return result.stream().collect(Collectors.toMap(TransportLineDto::getId, item -> item));
        });
    }

    public static CompletableFuture<Map<String, TransportTripsDto>> tripsMapFuture(TransportTripsFeign api, Set<String> tripsIdSet) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> list = tripsIdSet.stream().collect(Collectors.toList());
            List<TransportTripsDto> result = api.findAll(null, list);
            return result.stream().collect(Collectors.toMap(TransportTripsDto::getId, item -> item));
        });
    }

    public static CompletableFuture<Map<String, TruckDto>> truckMapFuture(TruckFeign api, Set<String> truckIdSet) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> list = truckIdSet.stream().collect(Collectors.toList());
            List<TruckDto> result = api.findAll(list, null);
            return result.stream().collect(Collectors.toMap(TruckDto::getId, item -> item));
        });
    }

    public static CompletableFuture<Map<Long, User>> driverMapFuture(UserApi api, Set<String> driverIdSet) {
        return CompletableFuture.supplyAsync(() -> {
            List<Long> list = driverIdSet.stream().filter(item -> StringUtils.isNotBlank(item)).map(item -> Long.parseLong(item)).collect(Collectors.toList());

            R<List<User>> result = api.list(list, null, null, null);
            if (result.getIsSuccess()) {
                return result.getData().stream().collect(Collectors.toMap(User::getId, item -> item));
            }
            return new HashMap<>();
        });
    }

    public static final CompletableFuture<Map<Long, Area>> areaMapFuture(AreaApi api, Long parentId, Set<Long> areaSet) {
        R<List<Area>> result = api.findAll(parentId, new ArrayList<>(areaSet));
        return CompletableFuture.supplyAsync(() ->
                result.getData().stream().collect(Collectors.toMap(Area::getId, vo -> vo)));
    }
}
