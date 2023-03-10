package com.itheima.pinda.service.impl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.pinda.DTO.*;
import com.itheima.pinda.DTO.transportline.TransportLineDto;
import com.itheima.pinda.entity.CacheLineDetailEntity;
import com.itheima.pinda.entity.CacheLineEntity;
import com.itheima.pinda.entity.CacheLineUseEntity;
import com.itheima.pinda.enums.ScheduleParams;
import com.itheima.pinda.feign.transportline.TransportLineFeign;
import com.itheima.pinda.service.ICacheLineDetailService;
import com.itheima.pinda.service.ICacheLineService;
import com.itheima.pinda.service.ICacheLineUseService;
import com.itheima.pinda.service.ITaskRoutePlanningService;
import com.itheima.pinda.utils.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 路线规划service
 */
@Slf4j
@Service
public class TaskRoutePlanningServiceImpl implements ITaskRoutePlanningService {
    @Autowired
    private TransportLineFeign transportLineFeign;
    @Autowired
    private ICacheLineService cacheLineService;
    @Autowired
    private ICacheLineDetailService cacheLineDetailService;
    @Autowired
    private ICacheLineUseService cacheLineUseService;

    /**
     * 路线规划
     * @param orderClassifyGroupDTOS
     * @param agencyId
     * @param jobId
     * @param logId
     * @param params
     * @return
     */
    public List<OrderLineSimpleDTO> execute(List<OrderClassifyGroupDTO> orderClassifyGroupDTOS, String agencyId, String jobId, String logId, String params) {
        List<OrderLineDTO> orderLineDTOS = new ArrayList<>();
        orderClassifyGroupDTOS.stream().filter(item -> "ERROR".equals(item.getKey())).forEach(item -> {
            // TODO 处理无法调度订单
            log.info("异常订单{}条", item.getOrders().size());
        });
        // 新订单调度
        orderClassifyGroupDTOS.stream().filter(item -> (item.isNew() && !"ERROR".equals(item.getKey()))).forEach(item -> {
            log.info("开始计算线路 {} -> {}", item.getStartAgencyId(), item.getEndAgencyId());
            // 查找到所有相关线路 （存在无法通行的情况，）
            RoutePlanningDTO routePlanningDTO = findLine(0, agencyId, item.getStartAgencyId(), item.getEndAgencyId(), new ArrayList<>());
            log.info("RoutePlanningDTO:{}", JSON.toJSONString(routePlanningDTO));
            // 查找到的线路分析，拆分，聚合。
            AnalysisRoutePlanningDTO analysisRoutePlanningDTO = new AnalysisRoutePlanningDTO(routePlanningDTO, item.getStartAgencyId(), item.getEndAgencyId()).build();
            Map<CacheLineEntity, List<CacheLineDetailEntity>> map = analysisRoutePlanningDTO.get();
            log.info("CacheLine:{}", JSON.toJSONString(map));
            log.info("线路分析完成：{}", map);
            // 与数据库中的数据和并
            map = mergeLine(map);
            // 选举出当前默认线路的第一阶段线路
            CacheLineDetailEntity cacheLineDetailEntity = defaultLine(map, params);
            if (cacheLineDetailEntity == null) {
                log.warn("线路获取异常");
                return;
            }
            // 线路关联 订单信息
            relationOrder(cacheLineDetailEntity, item.getId());
            // 返回线路信息
            orderLineDTOS.add(new OrderLineDTO(cacheLineDetailEntity, item));
        });
        // 中转订单调度
        orderClassifyGroupDTOS.stream().filter(item -> (!item.isNew() && !"ERROR".equals(item.getKey()))).forEach(item -> {
            log.info("开始查找线路 {} -> {}", item.getStartAgencyId(), item.getEndAgencyId());
            String id = item.getId();
            // 根据orderId 查到之前的线路
            CacheLineDetailEntity cacheLineDetailEntity = findBeforeLine(id, agencyId);
            // 返回线路信息
            orderLineDTOS.add(new OrderLineDTO(cacheLineDetailEntity, item));
        });

        // 构建模型
        List<OrderLineSimpleDTO> orderLineSimpleDTOS = buildOrderLineSimpleDTOS(orderLineDTOS);

        return orderLineSimpleDTOS;
    }

    /**
     * 构建精简订单分类模型
     * 根据不同线路的第一个中转点在此分配
     * 例：北京到上海  北京到深圳 两个种类订单，第一个中转点都会经过沿途的天津转运中心，那么这两个种类的订单就合成一类
     *
     * @param orderLineDTOS
     * @return
     */
    private List<OrderLineSimpleDTO> buildOrderLineSimpleDTOS(List<OrderLineDTO> orderLineDTOS) {
        Map<String, List<OrderClassifyGroupDTO>> orderClassifyGroupDTOMap = new HashMap<>();
        Map<String, CacheLineDetailEntity> cacheLineDetailEntityMap = new HashMap<>();
        orderLineDTOS.forEach(orderLineDTO -> {
            CacheLineDetailEntity cacheLineDetail = orderLineDTO.getCacheLineDetailEntity();
            OrderClassifyGroupDTO orderClassify = orderLineDTO.getOrderClassifyGroupDTO();

            List<OrderClassifyGroupDTO> orderClassifyGroup = new ArrayList<>();
            if (orderClassifyGroupDTOMap.containsKey(cacheLineDetail.getTransportLineId())) {
                orderClassifyGroupDTOMap.get(cacheLineDetail.getTransportLineId()).add(orderClassify);
            } else {
                orderClassifyGroup.add(orderClassify);
                orderClassifyGroupDTOMap.put(cacheLineDetail.getTransportLineId(), orderClassifyGroup);
            }
            cacheLineDetailEntityMap.put(cacheLineDetail.getTransportLineId(), cacheLineDetail);
        });

        List<OrderLineSimpleDTO> orderLineSimpleDTOS = new ArrayList<>();
        cacheLineDetailEntityMap.forEach((transportLineId, cacheLineDetail) -> {
            cacheLineDetail.setId(null);
            orderLineSimpleDTOS.add(new OrderLineSimpleDTO(cacheLineDetail, orderClassifyGroupDTOMap.get(transportLineId)));
        });
        return orderLineSimpleDTOS;
    }

    /**
     * 中转订单 查询之前计算好的线路
     *
     * @param id
     * @param agencyId
     * @return
     */
    private CacheLineDetailEntity findBeforeLine(String id, String agencyId) {
        log.info("查找之前使用的线路，分组id：{},机构id：{}", id, agencyId);
        LambdaQueryWrapper<CacheLineUseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CacheLineUseEntity::getOrderClassifyId, id);
        CacheLineUseEntity cacheLineUseEntity = cacheLineUseService.getOne(wrapper);
        log.info("查询到分组使用的线路信息:{}", cacheLineUseEntity);
        String cacheLineId = cacheLineUseEntity.getCacheLineId();
        LambdaQueryWrapper<CacheLineDetailEntity> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(CacheLineDetailEntity::getCacheLineId, cacheLineId);
        detailWrapper.eq(CacheLineDetailEntity::getStartAgencyId, agencyId);
        CacheLineDetailEntity cacheLineDetailEntity = cacheLineDetailService.getOne(detailWrapper);
        log.info("获取当前阶段路线信息:{}", cacheLineDetailEntity);
        return cacheLineDetailEntity;
    }

    /**
     * 关联线路与订单
     *
     * @param cacheLineDetailEntity
     * @param id
     */
    private void relationOrder(CacheLineDetailEntity cacheLineDetailEntity, String id) {
        CacheLineUseEntity cacheLineUseEntity = new CacheLineUseEntity();
        cacheLineUseEntity.setCacheLineId(cacheLineDetailEntity.getCacheLineId());
        cacheLineUseEntity.setOrderClassifyId(id);
        cacheLineUseEntity.setId(IdUtils.get());
        cacheLineUseService.save(cacheLineUseEntity);
    }

    /**
     * 根据定时任务参数选举默认线路
     *
     * @param map
     * @param params
     * @return
     */
    private CacheLineDetailEntity defaultLine(Map<CacheLineEntity, List<CacheLineDetailEntity>> map, String params) {
        if (map.size() == 0) {
            return null;
        }
        List<CacheLineEntity> cacheLineDtos = map.keySet().stream().collect(Collectors.toList());
        if (cacheLineDtos.size() == 1) {
            return map.get(cacheLineDtos.get(0)).get(0);
        }
        if (ScheduleParams.DISTANCE.getValue().equals(params))
            cacheLineDtos.sort(Comparator.comparing(CacheLineEntity::getDistance));
        if (ScheduleParams.COST.getValue().equals(params))
            cacheLineDtos.sort(Comparator.comparing(CacheLineEntity::getCost));
        if (ScheduleParams.ESTIMATEDTIME.getValue().equals(params))
            cacheLineDtos.sort(Comparator.comparing(CacheLineEntity::getEstimatedTime));
        if (ScheduleParams.TRANSFER.getValue().equals(params))
            cacheLineDtos.sort(Comparator.comparing(CacheLineEntity::getTransferCount));

        return map.get(cacheLineDtos.get(0)).get(0);
    }

    /**
     * 合并线路 若数据库中的线路与计算线路一致 则使用数据库中的线路 若不一致则新建线路
     *
     * @param map
     */
    private Map<CacheLineEntity, List<CacheLineDetailEntity>> mergeLine(Map<CacheLineEntity, List<CacheLineDetailEntity>> map) {
        Set<Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>>> entrySet = map.entrySet();
        Map<String, List<Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>>>> groupMap = new HashMap<>();
        entrySet.forEach(entry -> {
            String key = entry.getKey().getStartAgencyId() + entry.getKey().getEndAgencyId();
            if (groupMap.containsKey(key)) {
                groupMap.get(key).add(entry);
            } else {
                List<Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>>> list = new ArrayList<>();
                list.add(entry);
                groupMap.put(key, list);
            }
        });
        Map<String, Integer> deleteLindVerifyKey = new HashMap<>();
        for (Map.Entry<String, List<Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>>>> stringListEntry : groupMap.entrySet()) {
            // 同一 起始，终点的线路（多种方案）
            List<Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>>> list = stringListEntry.getValue();
            String notEqualsLineStart = null;
            String notEqualsLineEnd = null;
            for (Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>> cacheLineEntityListEntry : list) {
                CacheLineEntity cacheLineEntity = cacheLineEntityListEntry.getKey();
                String cacheLineId = cacheLineService.check(cacheLineEntity.getVerifyKey());
                if (StringUtils.isEmpty(cacheLineId)) {
                    // 线路不一致，
                    notEqualsLineStart = cacheLineEntity.getStartAgencyId();
                    notEqualsLineEnd = cacheLineEntity.getEndAgencyId();
                }
            }
            if (StringUtils.isNotBlank(notEqualsLineStart) && StringUtils.isNotBlank(notEqualsLineEnd)) {
                // 线路存在不一致情况，统一作废原线路。
                Integer version = cacheLineService.deleteOldAndGetNewVersion(notEqualsLineStart, notEqualsLineEnd);
                for (Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>> cacheLineEntityListEntry : list) {
                    //记录 作废的线路
                    deleteLindVerifyKey.put(cacheLineEntityListEntry.getKey().getVerifyKey(), version);
                }
            }

        }


        for (Map.Entry<CacheLineEntity, List<CacheLineDetailEntity>> cacheLineEntityListEntry : map.entrySet()) {
            CacheLineEntity cacheLineEntity = cacheLineEntityListEntry.getKey();
            List<CacheLineDetailEntity> cacheLineDetailEntities = cacheLineEntityListEntry.getValue();
            if (deleteLindVerifyKey.containsKey(cacheLineEntity.getVerifyKey())) {
                //已经作废的线路  新增
                cacheLineEntity.setVersion(deleteLindVerifyKey.get(cacheLineEntity.getVerifyKey()));
                cacheLineService.save(cacheLineEntity);
                cacheLineDetailEntities = cacheLineDetailEntities.stream().map(item -> {
                    item.setId(IdUtils.get());
                    return item;
                }).collect(Collectors.toList());
                cacheLineDetailService.saveBatch(cacheLineDetailEntities);
                map.put(cacheLineEntity, cacheLineDetailEntities);
            } else {
                // 未作废 查询
                String cacheLineId = cacheLineService.check(cacheLineEntity.getVerifyKey());
                cacheLineEntity.setId(cacheLineId);
                LambdaQueryWrapper<CacheLineDetailEntity> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(CacheLineDetailEntity::getCacheLineId, cacheLineId);
                cacheLineDetailEntities = cacheLineDetailService.list(wrapper);
                map.put(cacheLineEntity, cacheLineDetailEntities);
            }
        }

        return map;
    }

    /**
     * 递归查找线路
     *
     * @param depth          查找深度
     * @param start          开始机构
     * @param end            结束结构
     * @param superiorStarts 上级开始机构集合
     * @return
     */
    private RoutePlanningDTO findLine(int depth, String agencyId, String start, String end, List<String> superiorStarts) {
        RoutePlanningDTO routePlanningDTO = new RoutePlanningDTO(++depth);
        List<String> feasibleIds = new ArrayList<>();
        List<TransportLineDto> transportLineDtos = findDirectLine(start, end, agencyId);
        log.info("【{}】查找直达路线【{}】->【{}】：{}条 深度:{}", agencyId, start, end, transportLineDtos.size(), depth);
        if (transportLineDtos != null && transportLineDtos.size() > 0) {
            routePlanningDTO.addFeasible(transportLineDtos);
            feasibleIds.addAll(transportLineDtos.stream().map(item -> item.getId()).collect(Collectors.toList()));
        }

        transportLineDtos = findStartLine(start, agencyId);
        superiorStarts.add(start);
        // 1 过滤已经直达的路线，2 过滤上一级的机构（本次查询到的路线 终点不能是上一级或者在上级的机构，若不过滤，理论上会出现死循环）
        transportLineDtos = transportLineDtos.stream()
                .filter(item -> ((!feasibleIds.contains(item.getId())) && (!superiorStarts.contains(item.getEndAgencyId()))))
                .collect(Collectors.toList());
        log.info("【{}】查找起始机构路线【{}】->【{}】：{}条 深度:{}", agencyId, start, end, transportLineDtos.size(), depth);
        if (transportLineDtos.size() > 0) {
            routePlanningDTO.add(transportLineDtos);
            for (TransportLineDto transportLineDto : transportLineDtos) {
                String endAgencyId = transportLineDto.getEndAgencyId();
                RoutePlanningDTO routePlanningDTOD = findLine(depth, agencyId, endAgencyId, end, superiorStarts);
                routePlanningDTO.put(transportLineDto.getId(), routePlanningDTOD);
            }
        }
        return routePlanningDTO;
    }

    /**
     * 查找直达线路
     *
     * @return
     */
    private List<TransportLineDto> findDirectLine(String start, String end, String agencyId) {
        TransportLineDto transportLineDto = new TransportLineDto();
        transportLineDto.setStartAgencyId(start);
        transportLineDto.setEndAgencyId(end);
        //transportLineDto.setAgencyId(agencyId);
        transportLineDto.setStatus(1);
        return transportLineFeign.list(transportLineDto);
    }

    /**
     * 查找起始线路
     *
     * @param startAgency
     * @return
     */
    private List<TransportLineDto> findStartLine(String startAgency, String agencyId) {
        TransportLineDto transportLineDto = new TransportLineDto();
        transportLineDto.setStartAgencyId(startAgency);
        //transportLineDto.setAgencyId(agencyId);
        transportLineDto.setStatus(1);
        return transportLineFeign.list(transportLineDto);
    }
}