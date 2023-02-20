package com.itheima.pinda.common.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元对象
 *
 * @author: zhangdongjiang
 * @date: 2019-05-10 14:18
 */
@SuppressWarnings("unused")
public class MetaObject<T> {
    private T target;
    private Map<String, Field> fieldMap;

    /**
     * 初始化元对象
     *
     * @param target 目标对象
     */
    public MetaObject(T target) {
        this.target = target;
        if (target == null) {
            throw new NullPointerException("target不能为空");
        }
        fieldMap = getDeclaredField(target.getClass());
    }

    /**
     * 获取目标对象
     *
     * @return
     */
    public T getTarget() {
        return target;
    }

    /**
     * 获取字段列表
     *
     * @return
     */
    public List<String> getFieldList() {
        return fieldMap.keySet().stream().collect(Collectors.toList());
    }

    /**
     * 是否包含指定字段
     *
     * @param field
     * @return
     */
    public boolean containsField(String field) {
        return fieldMap.containsKey(field);
    }

    /**
     * 获取字段值
     *
     * @param field 要获取值的字段名
     * @return
     * @throws IllegalAccessException
     */
    public Object getFieldValue(String field) throws IllegalAccessException {
        if (!containsField(field)) {
            throw new IllegalAccessException(String.format("不包含名为%s的字段", field));
        }
        Field fieldItem = fieldMap.get(field);
        fieldItem.setAccessible(true);
        return fieldItem.get(target);
    }

    /**
     * 设置字段值
     *
     * @param field 要设置值的字段名
     * @param value 要设置的值
     * @return
     * @throws IllegalAccessException
     */
    public MetaObject setFieldValue(String field, Object value) throws IllegalAccessException {
        if (!containsField(field)) {
            throw new IllegalAccessException(String.format("不包含名为%s的字段", field));
        }
        Field fieldItem = fieldMap.get(field);
        fieldItem.setAccessible(true);
        fieldItem.set(target, value);
        return this;
    }

    /**
     * 获取类的所有字段
     *
     * @param clazz 要获取字段的类
     * @return
     */
    public static Map<String, Field> getDeclaredField(Class clazz) {
        Map<String, Field> fieldMap = getDeclaredField(clazz, null);
        return fieldMap;
    }

    /**
     * 获取类的所有字段
     *
     * @param clazz    要获取字段的类
     * @param fieldMap 字段Map
     * @return
     */
    public static Map<String, Field> getDeclaredField(Class clazz, Map<String, Field> fieldMap) {
        if (fieldMap == null) {
            fieldMap = new HashMap<>();
        }
        if (clazz != null) {
            Field[] objectDeclaredField = clazz.getDeclaredFields();
            for (Field item : objectDeclaredField) {
                String name = item.getName();
                if (!fieldMap.containsKey(name)) {
                    fieldMap.put(name, item);
                }
            }
            fieldMap = getDeclaredField(clazz.getSuperclass(), fieldMap);
        }
        return fieldMap;
    }
}
