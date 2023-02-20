package com.itheima.pinda.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 封装对属性的访问
 *
 * @author: zhangdongjiang
 * @date: 2019-05-10 16:09
 */
@Component
@SuppressWarnings("unused")
public class PropertyGenius {
    @Autowired
    private ApplicationContext applicationContext;
    private List<PropertySource> propertySourceList = new ArrayList<>();

    /**
     * 获取当前环境所有key集合
     *
     * @return
     */
    public List<String> getKeyList() {
        List<String> keyList = new ArrayList<>();
        AbstractEnvironment environment = (AbstractEnvironment) applicationContext.getEnvironment();
        for (PropertySource propertySource : environment.getPropertySources()) {
            propertySourceList.add(propertySource);
            if (propertySource instanceof MapPropertySource) {
                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                keyList.addAll(mapPropertySource.getSource().keySet());
            }
        }
        return keyList;
    }

    /**
     * 获取属性值
     *
     * @param key
     * @return
     */
    public Object getKeyValue(String key) {
        Optional<PropertySource> propertySource = propertySourceList.stream().filter(ps -> ps.containsProperty(key)).findFirst();
        if (propertySource.isPresent()) {
            return propertySource.get().getProperty(key);
        }
        return null;
    }

    /**
     * 转换key为字段名
     *
     * @param key
     * @return
     */
    public static String keyToFieldName(String key) {
        String fieldName = key;
        if (StringUtils.hasText(key)) {
            if (key.contains(".")) {
                String[] keys = key.split("\\.");
                key = keys[keys.length - 1];
            }
            if (key.contains("-")) {
                String[] keys = key.split("-");
                StringBuilder fieldNameBuilder = new StringBuilder();
                int index = 0;
                for (String item : keys) {
                    if (index > 0) {
                        fieldNameBuilder.append(item.substring(0, 1).toUpperCase());
                        fieldNameBuilder.append(item.substring(1));
                    } else {
                        fieldNameBuilder.append(item);
                    }
                    index++;
                }
                fieldName = fieldNameBuilder.toString();
            } else {
                fieldName = key;
            }
        }
        return fieldName;
    }

    public Map<String, PropertySource> getPropertySourceMap(String regex) {
        Map<String, PropertySource> propertySourceMap = new HashMap<>();
        AbstractEnvironment environment = (AbstractEnvironment) applicationContext.getEnvironment();
        Pattern pattern = Pattern.compile(regex);
        for (PropertySource propertySource : environment.getPropertySources()) {
            propertySourceList.add(propertySource);
            if (propertySource instanceof MapPropertySource) {
                MapPropertySource mapPropertySource = (MapPropertySource) propertySource;
                List<String> keys = mapPropertySource.getSource().keySet()
                        .stream().map(key -> {
                            Matcher matcher = pattern.matcher(key);
                            return matcher.find() ? matcher.group(0) : "";
                        }).filter(key -> StringUtils.hasText(key))
                        .distinct()
                        .collect(Collectors.toList());
                if (keys.size() > 0) {
                    keys.forEach(key -> propertySourceMap.put(key, propertySource));
                }
            }
        }
        return propertySourceMap;
    }
}
