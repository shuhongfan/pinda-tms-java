package com.itheima.j2cache.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    String region() default "rx";
    String key() default "";
    String params() default "";
}
