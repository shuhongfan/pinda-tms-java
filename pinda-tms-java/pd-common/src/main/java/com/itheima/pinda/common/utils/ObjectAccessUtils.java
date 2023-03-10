package com.itheima.pinda.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * 对象访问工具类
 * <br>
 * 支持的路径格式参考：
 * <p>
 * address.id
 * <br>
 * detailList[0].name
 * <br>
 * payInfo.payMethodList[0].name
 * <br>
 * 0.text
 * <br>
 * data.address.city.text
 * <br>
 * ?0.text
 * <br>
 * detailList[?0].name
 * <br>
 * data.address?.city?.text
 * </p>
 *
 * @author: zhangdongjiang
 * @date: 2019-06-11 10:38
 */
public class ObjectAccessUtils {
    /**
     * 以字段路径形式访问对象
     * <br>
     * 字段路径支持所有常见访问场景。比如：<br>
     * address.id <br>
     * detailList[0].name <br>
     * payInfo.payMethodList[0].name <br>
     * 0.text <br>
     * data.address.city.text <br>
     * ?0.text <br>
     * detailList[?2].name <br>
     * data.address?.city?.text <br>
     *
     * @param target    要访问的目标对象
     * @param fieldPath 字段路径
     * @param clazz     要返回的类型的类
     * @param <T>       要返回的类型
     * @return 值
     * @throws IllegalAccessException 当无法访问对象或者字段时触发
     */
    public static <T> T get(Object target, String fieldPath, Class<T> clazz) throws IllegalAccessException {
        return get(target, fieldPath, clazz, "");
    }

    /**
     * 以字段路径形式访问对象
     * <br>
     * 字段路径支持所有常见访问场景。比如：<br>
     * address.id <br>
     * detailList[0].name <br>
     * payInfo.payMethodList[0].name <br>
     * 0.text <br>
     * data.address.city.text <br>
     * ?0.text <br>
     * detailList[?2].name <br>
     * data.address?.city?.text <br>
     *
     * @param target    要访问的目标对象
     * @param fieldPath 字段路径
     * @param clazz     要返回的类型的类
     * @param delimiter 分隔符
     * @param <T>       要返回的类型
     * @return 值
     * @throws IllegalAccessException 当无法访问对象或者字段时触发
     */
    public static <T> T get(Object target, String fieldPath, Class<T> clazz, String delimiter) throws IllegalAccessException {
        return get(target, fieldPath, clazz, delimiter, null);
    }

    /**
     * 以字段路径形式访问对象
     * <br>
     * 字段路径支持所有常见访问场景。比如：<br>
     * address.id <br>
     * detailList[0].name <br>
     * payInfo.payMethodList[0].name <br>
     * 0.text <br>
     * data.address.city.text <br>
     * ?0.text <br>
     * detailList[?2].name <br>
     * data.address?.city?.text <br>
     *
     * @param target    要访问的目标对象
     * @param fieldPath 字段路径
     * @param clazz     要返回的类型的类
     * @param delimiter 分隔符
     * @param nullText  null时替代文本
     * @param <T>       要返回的类型
     * @return 值
     * @throws IllegalAccessException 当无法访问对象或者字段时触发
     */
    public static <T> T get(Object target, String fieldPath, Class<T> clazz, String delimiter, String nullText) throws IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException("要访问的目标对象不能为空");
        } else if (fieldPath == null) {
            throw new IllegalArgumentException("要访问的目标对象的字段路径不能为空");
        } else if (!StringUtils.hasText(fieldPath)) {
            throw new IllegalArgumentException("要访问的目标对象的字段路径不能为空");
        }
        fieldPath = fieldPath.replaceAll(",", "+");
        if (fieldPath.contains("+")) {
            if (!String.class.equals(clazz)) {
                throw new IllegalArgumentException("当字段路径中包含+时，clazz只能是String.class");
            }
            String[] fieldPathList = fieldPath.split("\\+");
            ArrayList<String> results = new ArrayList<>();
            if (StringUtils.hasText("s.")) {
                results.add(get(target, fieldPathList[0], String.class));
            } else {
                for (String fp : fieldPathList) {
                    if (StringUtils.hasText(fp)) {
                        String item = get(target, fp, String.class);
                        if (item == null) {
                            item = nullText;
                        }
                        results.add(item);
                    }
                }
            }
            return (T) String.join(delimiter, results);
        }

        if (fieldPath.startsWith("*")) {
            // fieldPath = fieldPath.substring(1);
            throw new IllegalArgumentException("路径不能以*开头");
        }
        JSON targetElement;
        if (target instanceof JSON) {
            targetElement = (JSON) target;
        } else {
            String jsonText = JSONObject.toJSONString(target);
            targetElement = (JSON) JSONObject.parse(jsonText);
        }
        if (targetElement == null) {
            throw new IllegalArgumentException("无法以json形式访问目标对象");
        }
        FieldAccessDescriptor fieldAccessDescriptor = FieldAccessDescriptor.parse(fieldPath);
        Object valueElement = getValue(targetElement, fieldAccessDescriptor);
        Object result;
        if (valueElement == null) {
            result = null;
        } else {
            if (valueElement.getClass().equals(clazz)) {
                result = valueElement;
            } else if (String.class.equals(clazz)) {
                result = JSONObject.toJSONString(valueElement);
            } else {
                result = JSONObject.parseObject(JSONObject.toJSONString(valueElement), clazz);
            }
        }
        if (result == null && String.class.equals(clazz)) {
            result = nullText;
        }
        return (T) result;
    }

    /**
     * 获取值
     *
     * @param targetElement         目标元素
     * @param fieldAccessDescriptor 访问标识符
     * @return 值
     * @throws IllegalAccessException 访问出错时抛出
     */
    private static Object getValue(Object targetElement, FieldAccessDescriptor fieldAccessDescriptor) throws IllegalAccessException {
        Object valueElement;
        boolean needBreak = false;
        if (fieldAccessDescriptor.accessArray()) {
            if (!(targetElement instanceof JSONArray)) {
                throw new IllegalAccessException("要访问的索引的目标不是对象：" + fieldAccessDescriptor.getCurrentIndex());
            }
            JSONArray jsonArray = (JSONArray) targetElement;
            if (fieldAccessDescriptor.getCurrentIndex() < 0 || jsonArray.size() <= fieldAccessDescriptor.getCurrentIndex()) {
                if (fieldAccessDescriptor.isOptionalAccess()) {
                    valueElement = null;
                    needBreak = true;
                } else {
                    throw new IndexOutOfBoundsException("索引超界：" + fieldAccessDescriptor.getCurrentPath());
                }
            } else {
                valueElement = jsonArray.get(fieldAccessDescriptor.getCurrentIndex());
            }
        } else {
            if (targetElement == null) {
                if (fieldAccessDescriptor.isOptionalAccess()) {
                    valueElement = null;
                    needBreak = true;
                } else {
                    throw new IllegalAccessException("无法访问对象" + fieldAccessDescriptor.getCurrentPath());
                }
            } else {
                if (!(targetElement instanceof JSONObject)) {
//                    throw new IllegalAccessException("要访问的字段的目标不是对象：" + fieldAccessDescriptor.getCurrentField());
                    if (targetElement instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) targetElement;
                        String s = "";
                        for (int i = 0; i < jsonArray.size(); i++) {
                            s += jsonArray.getString(i) + "_";
                        }
                        return s.substring(0, s.length() - 1);
                    }
                }
                JSONObject jsonObject = (JSONObject) targetElement;
                if (!jsonObject.containsKey(fieldAccessDescriptor.getCurrentField())) {
                    if (fieldAccessDescriptor.isOptionalAccess()) {
                        valueElement = null;
                        needBreak = true;
                    } else {
                        throw new IllegalAccessException("无法访问对象" + fieldAccessDescriptor.getCurrentPath());
                    }
                } else {
                    valueElement = jsonObject.get(fieldAccessDescriptor.getCurrentField());
                }
            }
        }
        if (!needBreak && fieldAccessDescriptor.hasNext()) {
            valueElement = getValue(valueElement, fieldAccessDescriptor.getNextFieldAccessDescriptor());
        }
        return valueElement;
    }
}
