package com.itheima.j2cache.aop.processor;

import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.annotation.CacheEvictor;
import com.itheima.j2cache.model.AnnotationInfo;
import com.itheima.j2cache.utils.CacheKeyBuilder;
import com.itheima.j2cache.utils.SpringApplicationContextUtils;
import net.oschina.j2cache.CacheChannel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 抽象缓存注解处理器
 */
public abstract class AbstractCacheAnnotationProcessor {
    protected CacheChannel cacheChannel;

    /**
     * 初始化公共属性，供子类使用
     */
    public AbstractCacheAnnotationProcessor(){
        ApplicationContext applicationContext = SpringApplicationContextUtils.getApplicationContext();
        cacheChannel = applicationContext.getBean(CacheChannel.class);
    }

    /**
     * 封装注解信息
     * @param proceedingJoinPoint
     * @param cache
     * @return
     */
    protected AnnotationInfo<Cache> getAnnotationInfo(ProceedingJoinPoint proceedingJoinPoint,Cache cache){
        AnnotationInfo<Cache> annotationInfo = new AnnotationInfo<>();
        annotationInfo.setAnnotation(cache);
        annotationInfo.setRegion(cache.region());
        try{
            annotationInfo.setKey(generateKey(proceedingJoinPoint,cache));
        }catch (Exception e){
            e.printStackTrace();
        }
        return annotationInfo;
    }

    /**
     * 动态解析注解信息，生成key
     * @param proceedingJoinPoint
     * @param cache
     * @return
     */
    protected String generateKey(ProceedingJoinPoint proceedingJoinPoint,Cache cache) throws IllegalAccessException{
        String key = cache.key();//ab
        if(!StringUtils.hasText(key)){
            //如果当前key为空串，重新设置当前可以为：目标Controller类名:方法名
            String className = proceedingJoinPoint.getTarget().getClass().getSimpleName();
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            String methodName = signature.getMethod().getName();
            key = className + ":" + methodName;
        }
        //ab:100
        key = CacheKeyBuilder.generate(key,cache.params(),proceedingJoinPoint.getArgs());
        return key;
    }

    /**
     * 抽象方法，处理缓存操作，具体应该由子类具体实现
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    public abstract Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;

    /**
     * 获得缓存注解处理器对象
     * @param proceedingJoinPoint
     * @param cache
     * @return
     */
    public static CachesAnnotationProcessor getProcessor(ProceedingJoinPoint proceedingJoinPoint, Cache cache){
        return new CachesAnnotationProcessor(proceedingJoinPoint,cache);
    }

    /**
     * 获得清理缓存注解处理器对象
     * @param proceedingJoinPoint
     * @param cacheEvictor
     * @return
     */
    public static CacheEvictorAnnotationProcessor getProcessor(ProceedingJoinPoint proceedingJoinPoint, CacheEvictor cacheEvictor){
        return new CacheEvictorAnnotationProcessor(proceedingJoinPoint,cacheEvictor);
    }
}
