package com.itheima.pinda.enums.transporttask;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 运输任务满载状态
 *
 * @author itcast
 */

public enum TransportTaskLoadingStatus {
    /**
     * 空载
     */
    EMPTY(1, "空载"),
    /**
     * 半载
     */
    HALF(2, "半载"),
    /**
     * 满载
     */
    FULL(3, "满载");


    TransportTaskLoadingStatus(Integer code, String value) {

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
    private static final Map<Integer, TransportTaskLoadingStatus> LOOKUP = new HashMap<>();

    //静态初始化
    static {

        for (TransportTaskLoadingStatus statusEnum : EnumSet.allOf(TransportTaskLoadingStatus.class)) {

            LOOKUP.put(statusEnum.code, statusEnum);
        }
    }

    /**
     * 根据code获取枚举项
     *
     * @param code 值
     * @return 值
     */
    public static TransportTaskLoadingStatus lookup(Integer code) {
        return LOOKUP.get(code);
    }

}
