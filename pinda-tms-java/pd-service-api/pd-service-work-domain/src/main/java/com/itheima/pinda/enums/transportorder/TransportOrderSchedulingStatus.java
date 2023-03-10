package com.itheima.pinda.enums.transportorder;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 运单-调度状态
 */

public enum TransportOrderSchedulingStatus {
    /**
     * 待调度
     */
    TO_BE_SCHEDULED(1, "待调度"),

    /**
     * 未匹配到线路
     */
    NO_MATCH_TRANSPORTLINE(2, "未匹配到线路"),

    /**
     * 已调度
     */
    SCHEDULED(3, "已调度");


    TransportOrderSchedulingStatus(Integer code, String value) {

        this.code = code;
        this.value = value;
    }

    /**
     * 类型编码
     */
    private final Integer code;

    /**
     * 类型值
     */
    private final String value;


    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


    /**
     * 循环变量
     */
    private static final Map<Integer, TransportOrderSchedulingStatus> LOOKUP = new HashMap<>();

    //静态初始化
    static {

        for (TransportOrderSchedulingStatus taskType : EnumSet.allOf(TransportOrderSchedulingStatus.class)) {

            LOOKUP.put(taskType.code, taskType);
        }
    }

    /**
     * 根据code获取枚举项
     *
     * @param code 值
     * @return 值
     */
    public static TransportOrderSchedulingStatus lookup(Integer code) {
        return LOOKUP.get(code);
    }

}
