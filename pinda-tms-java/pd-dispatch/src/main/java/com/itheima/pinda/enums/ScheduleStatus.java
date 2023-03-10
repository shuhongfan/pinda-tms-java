package com.itheima.pinda.enums;


public enum ScheduleStatus {
    /**
     * 暂停
     */
    PAUSE(0),
    /**
     * 正常
     */
    NORMAL(1);

    private int value;

    ScheduleStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}