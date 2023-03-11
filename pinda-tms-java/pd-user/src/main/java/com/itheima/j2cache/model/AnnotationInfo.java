package com.itheima.j2cache.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.annotation.Annotation;


/**
 * Cache信息包装
 */
public class AnnotationInfo<T extends Annotation> {
    private T annotation;
    private String key;
    private String region;

    public T getAnnotation() {
        return annotation;
    }

    public void setAnnotation(T cache) {
        this.annotation = cache;
    }

    public String getKey() {
        return key;
    }

    public String getRegion() {
        return region;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String toString() {
        if (annotation == null) {
            return null;
        }
        return JSONObject.toJSONString(this);
    }
}