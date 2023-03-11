package com.itheima.j2cache.aop.processor;

import com.itheima.j2cache.annotation.Cache;
import com.itheima.j2cache.model.AnnotationInfo;
import com.itheima.j2cache.model.CacheHolder;
import net.oschina.j2cache.CacheObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cache注解处理器
 */
public class CachesAnnotationProcessor extends AbstractCacheAnnotationProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CachesAnnotationProcessor.class);
    private AnnotationInfo<Cache> annotationInfo;

    /**
     * 初始化处理器，同时将相关的对象进行初始化
     * @param proceedingJoinPoint
     * @param cache
     */
    public CachesAnnotationProcessor(ProceedingJoinPoint proceedingJoinPoint, Cache cache) {
        super();
//        创建注解信息对象
        annotationInfo = getAnnotationInfo(proceedingJoinPoint, cache);
    }

    /**
     * 具体缓存处理逻辑
     * @param proceedingJoinPoint 切点
     * @return
     * @throws Throwable
     */
    @Override
    public Object process(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result=null;
        boolean existsCache = false;

//        1.获取缓存数据
        CacheHolder cacheHolder = getCache(annotationInfo);
        if (cacheHolder.isExistsCache()) {
//        2. 如果缓存数据存在则直接返回（相当于Controller的目标方法没有执行）
            result = cacheHolder.getValue();
            existsCache = true;
        }

//        3. 如何缓存数据不存在，放行调用Controller的目标方法
        if (!existsCache) {
            result = invoke(proceedingJoinPoint);
//        4. 将目标方法的返回值载入缓存
            setCache(result);
        }

//        5. 将结果返回
        return result;
    }

    /**
     * 尝试获取值
     *
     * @param proceedingJoinPoint 切点
     * @return 结果
     * @throws Throwable 异常时抛出
     */
    private Object invoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    /**
     * 设置缓存
     *
     * @param result 数据
     * @throws Throwable 异常时抛出
     */
    private void setCache(Object result) throws Throwable {
        cacheChannel.set(annotationInfo.getRegion(), annotationInfo.getKey(), result);
    }

    /**
     * 获取缓存数据
     * @param annotationInfo
     * @return
     */
    private CacheHolder getCache(AnnotationInfo<Cache> annotationInfo) {
        Object value = null;
        String region = annotationInfo.getRegion();
        String key = annotationInfo.getKey();
        boolean exists = cacheChannel.exists(region, key);
        if (exists) {
            CacheObject cacheObject = cacheChannel.get(region, key);
//            获得缓存结果数据
            value = cacheObject.getValue();
            return CacheHolder.newResult(value, true);
        }
        return CacheHolder.newResult(value, false);
    }
}
