package com.itheima.j2cache.aop.processor;

import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.annotation.CacheEvictor;
import com.itheima.j2cache.model.AnnotationInfo;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 清理缓存数据处理器
 */
public class CacheEvictorAnnotationProcessor extends AbstractCacheAnnotationProcessor{
    /**
     * 封装注解信息集合
     */
    private List<AnnotationInfo<Cache>> cacheList = new ArrayList<>();

    /**
     * 初始化清理缓存注解处理器对象，同时初始化一些缓存操作的对象
     * @param proceedingJoinPoint
     * @param cacheEvictor
     */
    public CacheEvictorAnnotationProcessor(ProceedingJoinPoint proceedingJoinPoint, CacheEvictor cacheEvictor) {
        super();
        Cache[] value = cacheEvictor.value();
        for(Cache cache : value){
            AnnotationInfo<Cache> annotationInfo = getAnnotationInfo(proceedingJoinPoint, cache);
            cacheList.add(annotationInfo);
        }
    }

    /**
     * 具体清理缓存处理逻辑
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        for (AnnotationInfo<Cache> annotationInfo : cacheList) {
            String region = annotationInfo.getRegion();
            String key = annotationInfo.getKey();
            //清理缓存数据
            cacheChannel.evict(region,key);
        }
        //调用目标方法（就是Controller中的方法）
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }
}
