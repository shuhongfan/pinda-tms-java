package com.itheima.pinda.DTO;

import com.itheima.pinda.DTO.transportline.TransportLineDto;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Data
public class RoutePlanningDTO {
    private int depth;

    /**
     * 直达线路集合
     */
    private List<TransportLineDto> feasibleTransportLineDtos;

    /**
     * 起始机构 可到达其他机构的线路
     */
    private List<TransportLineDto> transportLineDtos;

    /**
     * 下级 key为当前 transportLineDtos 的id
     */
    private Map<String, RoutePlanningDTO> routePlanningDTOMap;

    public RoutePlanningDTO(int depth) {
        transportLineDtos = new ArrayList<>();
        this.depth = depth;
    }

    public List<TransportLineDto> getFeasible() {
        return this.feasibleTransportLineDtos;
    }

    public void addFeasible(List<TransportLineDto> transportLineDtos) {
        if (transportLineDtos != null && transportLineDtos.size() > 0) {
            this.feasibleTransportLineDtos = (transportLineDtos);
        }
    }

    public RoutePlanningDTO get(TransportLineDto transportLineDto) {
        return this.routePlanningDTOMap.get(transportLineDto.getId());
    }

    public List<TransportLineDto> getNext() {
        return this.transportLineDtos;
    }

    public void add(List<TransportLineDto> transportLineDtos) {
        this.transportLineDtos = (transportLineDtos);
        routePlanningDTOMap = new HashMap<>();
    }


    public void put(String id, RoutePlanningDTO routePlanningDTO) {
        if (routePlanningDTO != null) {
            routePlanningDTOMap.put(id, routePlanningDTO);
        }
    }
}
