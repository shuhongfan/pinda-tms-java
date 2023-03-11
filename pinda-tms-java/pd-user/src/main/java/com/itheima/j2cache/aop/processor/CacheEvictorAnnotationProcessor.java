package com.itheima.j2cache.aop.processor;

import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.annotation.CacheEvictor;
import com.itheima.j2cache.model.AnnotationInfo;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.List;

/**
 * 失效缓存注解处理器
 */
public class CacheEvictorAnnotationProcessor extends AbstractCacheAnnotationProcessor {

//    封装注解信息集合
    private List<AnnotationInfo<Cache>> cacheList;


    /**
     * 初始化清理缓存注解处理器对象，同时初始化一些缓存操作的对象
     * @param proceedingJoinPoint
     */
    public CacheEvictorAnnotationProcessor(ProceedingJoinPoint proceedingJoinPoint,CacheEvictor cacheEvictor) {
        super();
        Cache[] value = cacheEvictor.value();
        for (Cache cache : value) {
            AnnotationInfo<Cache> annotationInfo = getAnnotationInfo(proceedingJoinPoint, cache);
            cacheList.add(annotationInfo);
        }
    }

    @Override
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        for (AnnotationInfo<Cache> cacheAnnotationInfo : cacheList) {
            String region = cacheAnnotationInfo.getRegion();
            String key = cacheAnnotationInfo.getKey();
//            清理缓存数据
            cacheChannel.evict(region, key);
        }

//        调用目标方法
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }
}
