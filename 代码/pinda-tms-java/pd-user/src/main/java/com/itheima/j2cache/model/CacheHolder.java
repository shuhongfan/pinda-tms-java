package com.itheima.j2cache.model;

/**
 * 缓存结果封装
 */
public class CacheHolder {
    private Object value;//缓存的数据
    private boolean existsCache;//缓存数据是否存在
    private Throwable throwable;

    /**
     * 初始化缓存占位
     */
    private CacheHolder() {
    }

    /**
     * 获取值
     *
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * 是否存在缓存
     *
     * @return
     */
    public boolean isExistsCache() {
        return existsCache;
    }

    /**
     * 是否有错误
     *
     * @return
     */
    public boolean hasError() {
        return throwable != null;
    }

    /**
     * 生成缓存结果的占位
     *
     * @param value       结果
     * @param existsCache 是否存在缓存
     * @return 缓存
     */
    public static CacheHolder newResult(Object value, boolean existsCache) {
        CacheHolder cacheHolder = new CacheHolder();
        cacheHolder.value = value;
        cacheHolder.existsCache = existsCache;
        return cacheHolder;
    }

    /**
     * 生成缓存异常的占位
     *
     * @param throwable 异常
     * @return 缓存
     */
    public static CacheHolder newError(Throwable throwable) {
        CacheHolder cacheHolder = new CacheHolder();
        cacheHolder.throwable = throwable;
        return cacheHolder;
    }
}
