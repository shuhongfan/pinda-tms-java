package com.itheima.pinda.DTO.angency;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class AgencyScopeDto implements Serializable {
    private static final long serialVersionUID = 3515302916861385887L;
    /**
     * id
     */
    private String id;
    /**
     * 机构id
     */
    private String agencyId;

    /**
     * 行政区域id
     */
    private String areaId;

    /**
     * 多边形经纬度坐标集合
     */
    private List<List<Map>> mutiPoints;
}
