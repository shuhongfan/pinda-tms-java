package com.itheima.pinda.common.utils.concurrent;

/**
 * 执行器回调
 *
 * @author: zhangdongjiang
 * @date: 2019-06-26 14:43
 */
@SuppressWarnings("unused")
public interface ExecutorCallback<T> {
    /**
     * 获得执行许可时回调
     *
     * @return 结果
     * @throws Throwable 异常
     */
    T acquire() throws Throwable;

    /**
     * 未获得执行许可时回调
     *
     * @return 结果
     * @throws Throwable 异常
     */
    T noAcquire() throws Throwable;
}
