package com.itheima.pinda.common.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态枚举
 * @author itcast
 */
public enum OrderEnum {

    /**
     * 待取件
     */
    ORDER(11001, "ORDER"),

    /**
     * 拒收
     */
    REJECTION(11002, "REJECTION"),

    /**
     * 已取消
     */
    CANCELLED(130003, "CANCELLED"),

    /**
     * 派送中
     */
    DISPATCHING(12004, "DISPATCHING"),

    /**
     * 派送完成
     */
    DISPATCHED(12005, "DISPATCHED"),

    /**
     * 已签收
     */
    RECEIVED(13004, "RECEIVED");

    OrderEnum(Integer code, String value) {

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
    private static final Map<Integer, OrderEnum> LOOKUP = new HashMap<>();

    //静态初始化
    static {

        for (OrderEnum orderEnum : EnumSet.allOf(OrderEnum.class)) {

            LOOKUP.put(orderEnum.code, orderEnum);
        }
    }

    /**
     * 根据code获取枚举项
     *
     * @param code 值
     * @return 值
     */
    public static OrderEnum lookup(Integer code) {

        return LOOKUP.get(code);
    }
}