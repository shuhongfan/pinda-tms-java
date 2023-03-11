package com.itheima.j2cache.annotation;

import java.lang.annotation.*;


/**
 * 清理缓存注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheEvictor {
    Cache[] value() default {};
}
