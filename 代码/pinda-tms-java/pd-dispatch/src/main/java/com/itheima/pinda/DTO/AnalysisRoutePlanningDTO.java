package com.itheima.pinda.DTO;

import com.alibaba.nacos.common.utils.Md5Utils;
import com.itheima.pinda.DTO.transportline.TransportLineDto;
import com.itheima.pinda.entity.CacheLineDetailEntity;
import com.itheima.pinda.entity.CacheLineEntity;
import com.itheima.pinda.utils.IdUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

public class AnalysisRoutePlanningDTO {
    private Map<Integer, List<CacheLineDetailEntity>> lineMap = new HashMap<>();
    private Map<String, TransportLineDto> transportLineDtoMap = new HashMap<>();

    private RoutePlanningDTO routePlanningDTO;
    private String startAgencyId;
    private String endAgencyId;

    private Integer index = 0;

    /**
     * 在此加工可用线路信息 返回完整的数据格式
     */
    public Map<CacheLineEntity, List<CacheLineDetailEntity>> get() {
        Map<CacheLineEntity, List<CacheLineDetailEntity>> result = new HashMap<>();
        for (List<CacheLineDetailEntity> value : lineMap.values()) {
            // 集合反转
            Collections.reverse(value);
            String cacheLineId = IdUtils.get();
            BigDecimal distance = new BigDecimal(0);
            BigDecimal cost = new BigDecimal(0);
            BigDecimal estimatedTime = new BigDecimal(0);
            StringBuffer sbf = new StringBuffer();
            for (int i = 0; i < value.size(); i++) {
                CacheLineDetailEntity cacheLineDetailEntity = value.get(i);
                cacheLineDetailEntity.setCacheLineId(cacheLineId);
                cacheLineDetailEntity.setSort(i + 1);
                String transportLineId = cacheLineDetailEntity.getTransportLineId();
                TransportLineDto transportLineDto = transportLineDtoMap.get(transportLineId);
                distance = distance.add(transportLineDto.getDistance());
                cost = cost.add(transportLineDto.getCost());
                estimatedTime = estimatedTime.add(transportLineDto.getEstimatedTime());
                sbf.append(transportLineDto.getStartAgencyId())
                        .append("#")
                        .append(transportLineDto.getEndAgencyId())
                        .append("#")
                        .append(transportLineDto.getDistance().toString())
                        .append("#")
                        .append(transportLineDto.getCost().toString())
                        .append("#")
                        .append(transportLineDto.getEstimatedTime().toString())
                        .append("#");

            }
            CacheLineEntity cacheLineEntity = new CacheLineEntity();
            cacheLineEntity.setId(cacheLineId);
            cacheLineEntity.setDistance(distance);
            cacheLineEntity.setCost(cost);
            cacheLineEntity.setEstimatedTime(estimatedTime);
            cacheLineEntity.setStartAgencyId(this.startAgencyId);
            cacheLineEntity.setEndAgencyId(this.endAgencyId);
            cacheLineEntity.setTransferCount(value.size());
            cacheLineEntity.setIsCurrent(1);
            cacheLineEntity.setVerifyKey(Md5Utils.getMD5(sbf.toString().getBytes()));
            result.put(cacheLineEntity, value);
        }
        return result;
    }

    public AnalysisRoutePlanningDTO(RoutePlanningDTO routePlanningDTO, String startAgencyId, String endAgencyId) {
        this.routePlanningDTO = routePlanningDTO;
        this.startAgencyId = startAgencyId;
        this.endAgencyId = endAgencyId;
    }

    public AnalysisRoutePlanningDTO build() {
        build(this.routePlanningDTO);
        return this;
    }

    /**
     * 获取全部可到达线路
     * 遍历之前查找到的线路，feasibleTransportLineDtos 不为空的时候表示该条线路可以通行。
     * @param routePlanningDTO
     * @return
     */
    private Integer[] build(RoutePlanningDTO routePlanningDTO) {
        List<Integer> indexList = new ArrayList<>();
        List<TransportLineDto> dtos = routePlanningDTO.getNext();
        if (!CollectionUtils.isEmpty(dtos)) {
            for (int i = 0; i < dtos.size(); i++) {
                TransportLineDto item = dtos.get(i);
                RoutePlanningDTO routePlanning = routePlanningDTO.get(item);
                Integer[] result = build(routePlanning);
                if (result.length > 0) {
                    // 含有可行线路，增加上级中转点
                    for (Integer integer : result) {
                        CacheLineDetailEntity cacheLineDetailEntity = new CacheLineDetailEntity();
                        cacheLineDetailEntity.setStartAgencyId(item.getStartAgencyId());
                        cacheLineDetailEntity.setEndAgencyId(item.getEndAgencyId());
                        cacheLineDetailEntity.setTransportLineId(item.getId());
                        lineMap.get(integer).add(cacheLineDetailEntity);
                        indexList.add(integer);
                        transportLineDtoMap.put(item.getId(), item);
                    }
                }
            }
        }

        List<TransportLineDto> feasibleDtos = routePlanningDTO.getFeasible();
        if (!CollectionUtils.isEmpty(feasibleDtos)) {
            for (int i = 0; i < feasibleDtos.size(); i++) {
                TransportLineDto item = feasibleDtos.get(i);
                // 可行路线  增加路线集合数据
                CacheLineDetailEntity cacheLineDetailEntity = new CacheLineDetailEntity();
                cacheLineDetailEntity.setStartAgencyId(item.getStartAgencyId());
                cacheLineDetailEntity.setEndAgencyId(item.getEndAgencyId());
                cacheLineDetailEntity.setTransportLineId(item.getId());
                List<CacheLineDetailEntity> cacheLineDetailEntities = new ArrayList<>();
                cacheLineDetailEntities.add(cacheLineDetailEntity);
                lineMap.put(++index, cacheLineDetailEntities);
                indexList.add(index);
                transportLineDtoMap.put(item.getId(), item);
            }
        }
        if (indexList.size() > 0) {
            return indexList.toArray(new Integer[indexList.size()]);
        } else {
            return new Integer[0];
        }
    }

}
