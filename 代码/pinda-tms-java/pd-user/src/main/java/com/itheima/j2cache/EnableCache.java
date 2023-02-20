package com.itheima.j2cache;
import com.itheima.j2cache.aop.CacheMethodInterceptor;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * 开启声明式缓存功能
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(CacheMethodInterceptor.class)
public @interface EnableCache {
}
