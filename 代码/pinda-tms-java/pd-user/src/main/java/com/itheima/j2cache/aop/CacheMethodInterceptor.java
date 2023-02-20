package com.itheima.j2cache.aop;

import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.annotation.CacheEvictor;
import com.itheima.j2cache.aop.processor.AbstractCacheAnnotationProcessor;
import com.itheima.j2cache.aop.processor.CacheEvictorAnnotationProcessor;
import com.itheima.j2cache.aop.processor.CachesAnnotationProcessor;
import com.itheima.j2cache.utils.SpringApplicationContextUtils;
import org.aopalliance.intercept.Interceptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 缓存操作拦截器
 */

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)//指定使用cglib方式为Controller创建代理对象，代理对象其实是目标对象的子类
@Import(SpringApplicationContextUtils.class)
public class CacheMethodInterceptor implements Interceptor{

    /**
     * 拦截方法上使用Cache注解的Controller
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.itheima.j2cache.annotation.Cache)")//环绕通知
    public Object invokeCacheAllMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        //获得方法前面对象
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        //获得当前拦截到的Controller方法对象
        Method method = signature.getMethod();
        //获得方法上的Cache注解信息
        Cache cache = AnnotationUtils.findAnnotation(method, Cache.class);
        if(cache != null){
            System.out.println("需要进行设置缓存数据处理...");
            //创建处理器，具体处理缓存逻辑
            CachesAnnotationProcessor processor = AbstractCacheAnnotationProcessor.getProcessor(proceedingJoinPoint, cache);
            return processor.process(proceedingJoinPoint);
        }
        //没有获取到Cache注解信息，直接放行
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    /**
     * 拦截方法上使用CacheEvictor注解的Controller
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.itheima.j2cache.annotation.CacheEvictor)")//环绕通知
    public Object invokeCacheEvictorAllMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        CacheEvictor cacheEvictor = AnnotationUtils.findAnnotation(method, CacheEvictor.class);
        if(cacheEvictor != null){
            System.out.println("清理缓存处理...");
            //创建清理缓存的处理器
            CacheEvictorAnnotationProcessor processor = AbstractCacheAnnotationProcessor.getProcessor(proceedingJoinPoint, cacheEvictor);
            return processor.process(proceedingJoinPoint);
        }

        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

}
