package com.itheima.j2cache.model;

import com.alibaba.fastjson.JSONObject;
import java.lang.annotation.Annotation;

/**
 * Cache相关信息封装
 */
public class AnnotationInfo<T extends Annotation> {
    private T annotation;
    private String region;
    private String key;//region:key:params

    public T getAnnotation() {
        return annotation;
    }

    public void setAnnotation(T annotation) {
        this.annotation = annotation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String toString() {
        if (annotation == null) {
            return null;
        }
        return JSONObject.toJSONString(this);
    }
}
