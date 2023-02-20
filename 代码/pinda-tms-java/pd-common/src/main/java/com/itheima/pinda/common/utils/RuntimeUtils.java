package com.itheima.pinda.common.utils;

import org.springframework.util.StringUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 运行时工具类
 *
 * @author: zhangdongjiang
 * @date: 2019-05-17 18:51
 */
@SuppressWarnings("unused")
public class RuntimeUtils {
    private static String runtimeName = "";
    private static Long pid = 0L;

    /**
     * 获取Runtime运行时名称
     *
     * @return Runtime运行时名称
     */
    public static String getRuntimeName() {
        if (!StringUtils.hasText(runtimeName)) {
            synchronized (runtimeName) {
                if (!StringUtils.hasText(runtimeName)) {
                    RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                    runtimeName = runtime.getName();
                }
            }
        }
        return runtimeName;
    }

    /**
     * 获取当前进程ID
     *
     * @return 当前进程ID
     */
    public static Long getPid() {
        if (pid == null || pid.longValue() < 1L) {
            synchronized (runtimeName) {
                if (pid == null || pid.longValue() < 1L) {
                    pid = Long.parseLong(getRuntimeName().split("@")[0]);
                }
            }
        }
        return pid;
    }

    /**
     * 获取当前线程ID
     *
     * @return 当前线程ID
     */
    public static long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }
}
