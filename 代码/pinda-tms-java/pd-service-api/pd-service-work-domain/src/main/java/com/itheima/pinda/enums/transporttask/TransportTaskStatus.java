package com.itheima.pinda.enums.transporttask;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 运输任务状态
 *
 * @author itcast
 */

public enum TransportTaskStatus {
    /**
     * 待执行,对应 待提货
     */
    PENDING(1, "待执行"),
    /**
     * 进行中，对应 在途
     */
    PROCESSING(2, "进行中"),
    /**
     * 待确认，保留状态
     */
    CONFIRM(3, "待确认"),
    /**
     * 已完成，对应 已交付
     */
    COMPLETED(4, "已完成"),
    /**
     * 已取消
     */
    CANCELLED(5, "已取消");


    TransportTaskStatus(Integer code, String value) {

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
    private static final Map<Integer, TransportTaskStatus> LOOKUP = new HashMap<>();

    //静态初始化
    static {

        for (TransportTaskStatus statusEnum : EnumSet.allOf(TransportTaskStatus.class)) {

            LOOKUP.put(statusEnum.code, statusEnum);
        }
    }

    /**
     * 根据code获取枚举项
     *
     * @param code 值
     * @return 值
     */
    public static TransportTaskStatus lookup(Integer code) {
        return LOOKUP.get(code);
    }

}
