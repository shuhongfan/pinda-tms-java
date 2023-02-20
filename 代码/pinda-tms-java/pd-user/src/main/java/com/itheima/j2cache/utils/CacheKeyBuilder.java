package com.itheima.j2cache.utils;
import org.springframework.util.StringUtils;
/**
 * 缓存键生成工具
 */
public class CacheKeyBuilder {
    /**
     * 生成key
     *
     * @param key      键
     * @param params   参数
     * @param args     参数值
     * @return
     * @throws IllegalAccessException 当访问异常时抛出
     */
    public static String generate(String key, String params, Object[] args) throws IllegalAccessException {
        StringBuilder keyBuilder = new StringBuilder("");
        if (StringUtils.hasText(key)) {
            keyBuilder.append(key);
        }
        if (StringUtils.hasText(params)) {
            String paramsResult = ObjectAccessUtils.get(args, params, String.class, "_", "null");
            keyBuilder.append(":");
            keyBuilder.append(paramsResult);
        }
        return keyBuilder.toString();
    }
}
