package com.itheima.j2cache.aop.processor;

import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.annotation.CacheEvictor;
import com.itheima.j2cache.model.AnnotationInfo;
import com.itheima.j2cache.utils.CacheKeyBuilder;
import com.itheima.j2cache.utils.SpringApplicationContextUtils;
import net.oschina.j2cache.CacheChannel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 抽象注解处理器
 */
public abstract class AbstractCacheAnnotationProcessor{
    protected CacheChannel cacheChannel;

    /**
     * 初始化公共属性，供子类使用
     */
    public AbstractCacheAnnotationProcessor() {
        ApplicationContext applicationContext = SpringApplicationContextUtils.getApplicationContext();
        cacheChannel = applicationContext.getBean(CacheChannel.class);
    }

    /**
     * 封装注解信息
     * @param proceedingJoinPoint
     * @param cache
     * @return
     */
    protected AnnotationInfo<Cache> getAnnotationInfo(ProceedingJoinPoint proceedingJoinPoint, Cache cache) {
        AnnotationInfo<Cache> annotationInfo = new AnnotationInfo<>();
        annotationInfo.setAnnotation(cache);
        annotationInfo.setRegion(cache.region());
        try {
            annotationInfo.setKey(generateKey(proceedingJoinPoint, annotationInfo.getAnnotation()));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("生成键出错：", e);
        }
        return annotationInfo;
    }

    /**
     * 动态解析注解信息，生成key
     * @param proceedingJoinPoint
     * @param cache
     * @return
     */
    protected String generateKey(ProceedingJoinPoint proceedingJoinPoint, Cache cache) throws IllegalAccessException {
        String key = cache.key();

//            如果当前key为空串，重新设置当前可以为：目标Controller类名:方法名
        if (!StringUtils.hasText(key)) {
//            获取类名
            String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
//            获取方法签名
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
//            获取方法名
            Method methodName = methodSignature.getMethod();
//            目标Controller类名:方法名
            key = className + ":" + methodName.getName();
        }

//        生成key
        key = CacheKeyBuilder.generate(key, cache.params(), proceedingJoinPoint.getArgs());
        return key;
    }

    /**
     * 处理
     *
     * @param proceedingJoinPoint 切点
     * @return 处理结果
     */
    public abstract Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

    /**
     * 获取注解处理器
     *
     * @param proceedingJoinPoint 切点
     * @param cache               注解
     * @return 注解处理器
     */
    public static AbstractCacheAnnotationProcessor getProcessor(ProceedingJoinPoint proceedingJoinPoint, Cache cache) {
        return new CachesAnnotationProcessor(proceedingJoinPoint, cache);
    }

    /**
     * 获取注解处理器
     *
     * @param proceedingJoinPoint 切点
     * @param cacheEvictor        注解
     * @return 注解处理器
     */
    public static AbstractCacheAnnotationProcessor getProcessor(ProceedingJoinPoint proceedingJoinPoint, CacheEvictor cacheEvictor) {
        return new CacheEvictorAnnotationProcessor(proceedingJoinPoint, cacheEvictor);
    }
}