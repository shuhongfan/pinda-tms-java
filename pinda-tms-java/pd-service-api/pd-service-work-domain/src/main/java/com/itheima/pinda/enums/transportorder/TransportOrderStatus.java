package com.itheima.pinda.enums.transportorder;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 运单-状态
 */

public enum TransportOrderStatus {
    /**
     * 新建
     */
    CREATED(1, "新建"),

    /**
     * 已装车，发往x转运中心
     */
    LOADED(2, "已装车"),

    /**
     * 到达
     */
    ARRIVED(3, "到达"),
    /**
     * 到达终端网点
     */
    ARRIVED_END(4, "到达终端网点"),
    /**
     * 已签收
     */
    RECEIVED(5, "已签收"),
    /**
     * 拒收
     */
    REJECTED(6, "拒收");


    TransportOrderStatus(Integer code, String value) {

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
    private static final Map<Integer, TransportOrderStatus> LOOKUP = new HashMap<>();

    //静态初始化
    static {

        for (TransportOrderStatus taskType : EnumSet.allOf(TransportOrderStatus.class)) {

            LOOKUP.put(taskType.code, taskType);
        }
    }

    /**
     * 根据code获取枚举项
     *
     * @param code 值
     * @return 值
     */
    public static TransportOrderStatus lookup(Integer code) {
        return LOOKUP.get(code);
    }

}
