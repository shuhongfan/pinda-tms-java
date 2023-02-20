package com.itheima.pinda.enums.transporttask;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 运输任务分配状态
 *
 * @author itcast
 */

public enum TransportTaskAssignedStatus {
    /**
     * 待分配
     */
    TO_BE_DISTRIBUTED(1, "待分配"),
    /**
     * 已分配
     */
    DISTRIBUTED(2, "已分配"),
    /**
     * 待人工分配
     */
    MANUAL_DISTRIBUTED(3, "待人工分配");


    TransportTaskAssignedStatus(Integer code, String value) {

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
    private static final Map<Integer, TransportTaskAssignedStatus> LOOKUP = new HashMap<>();

    //静态初始化
    static {

        for (TransportTaskAssignedStatus statusEnum : EnumSet.allOf(TransportTaskAssignedStatus.class)) {

            LOOKUP.put(statusEnum.code, statusEnum);
        }
    }

    /**
     * 根据code获取枚举项
     *
     * @param code 值
     * @return 值
     */
    public static TransportTaskAssignedStatus lookup(Integer code) {
        return LOOKUP.get(code);
    }

}
