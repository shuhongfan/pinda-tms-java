package com.itheima.pinda.enums;


public enum ScheduleParams {

    /**
     * 路线最近
     */
    DISTANCE("distance"),
    /**
     * 成本最低
     */
    COST("cost"),
    /**
     * 用时最少
     */
    ESTIMATEDTIME("estimatedTime"),
    /**
     * 中转次数最少
     */
    TRANSFER("transfer");

    private String value;

    ScheduleParams(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}