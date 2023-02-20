package com.itheima.pinda.common.utils;

import com.itheima.pinda.common.utils.StringGenius;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 为访问对象提供路径支持
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
 * @date: 2019-06-11 10:40
 */
public class FieldAccessDescriptor {
    private static ConcurrentHashMap<String, FieldAccessDescriptor> cacheMap = new ConcurrentHashMap<>(128);
    private String currentPath;
    private String currentField;
    private int currentIndex;
    private boolean isOptionalAccess;
    private FieldAccessDescriptor nextFieldAccessDescriptor;

    private FieldAccessDescriptor(String fieldPath) {
        this(fieldPath, true);
    }

    private FieldAccessDescriptor(String fieldPath, boolean isFirst) {
        try {
            // 移除[和.符号
            if (fieldPath.startsWith("[") || fieldPath.startsWith(".")) {
                fieldPath = fieldPath.substring(1);
            }
            if (fieldPath.startsWith("?")) {
                this.isOptionalAccess = true;
                // 移除可选访问符[?]
                fieldPath = fieldPath.substring(1);
                // 如果是逗点开头，则移除逗点[.]
                if (fieldPath.startsWith(".")) {
                    fieldPath = fieldPath.substring(1);
                }
            }
            int bracketIndex = fieldPath.indexOf('[');
            int commaIndex = fieldPath.indexOf('.');
            int optionalIndex = fieldPath.indexOf('?');
            List<Integer> indexList = Arrays.asList(bracketIndex, commaIndex, optionalIndex).stream().filter(i -> i > -1).collect(Collectors.toList());
            int index = -1;
            if (indexList.size() > 0) {
                index = indexList.stream().min((i, i2) -> (i - i2)).get().intValue();
            }
            if (index < 0) {
                // 解析终止
                // 移除可能存在的右中括号
                fieldPath = fieldPath.replace("]", "");
                int accessIndex = getInt(fieldPath);
                this.currentIndex = accessIndex;
                this.currentField = fieldPath;
                if (accessIndex > -1) {
                    this.currentPath = "[" + accessIndex + "]";
                } else {
                    this.currentPath = fieldPath;
                }
            } else {
                String left = fieldPath.substring(0, index);
                if (left == "") {
                    throw new IllegalArgumentException("非法的fieldPath格式");
                }
                String right = fieldPath.substring(index);
                left = left.replace("]", "");
                int accessIndex = getInt(left);
                this.currentIndex = accessIndex;
                this.currentField = left;
                if (accessIndex > -1) {
                    this.currentPath = "[" + accessIndex + "]";
                } else {
                    this.currentPath = left;
                }
                if (right != "") {
                    this.nextFieldAccessDescriptor = new FieldAccessDescriptor(right, false);
                }
            }
            if (isFirst) {
                fixPath();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("非法的fieldPath格式");
        }
    }

    private void fixPath() {
        if (hasNext()) {
            String separator = this.nextFieldAccessDescriptor.currentIndex > -1 ? "" : ".";
            this.nextFieldAccessDescriptor.currentPath = this.currentPath + separator + this.nextFieldAccessDescriptor.currentPath;
            this.nextFieldAccessDescriptor.fixPath();
        }
    }

    private int getInt(String text) {
        if (StringGenius.isInteger(text)) {
            return Integer.parseInt(text);
        } else {
            return -1;
        }
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public String getCurrentField() {
        return currentField;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean isOptionalAccess() {
        return isOptionalAccess;
    }

    public FieldAccessDescriptor getNextFieldAccessDescriptor() {
        return nextFieldAccessDescriptor;
    }

    public boolean accessArray() {
        return this.currentIndex > -1;
    }

    public boolean hasNext() {
        return this.nextFieldAccessDescriptor != null;
    }

    public String nextPath() {
        if (!hasNext()) {
            return "";
        } else {
            if (this.nextFieldAccessDescriptor.accessArray()) {
                return String.format("[%s]", this.nextFieldAccessDescriptor.getCurrentIndex());
            } else {
                return this.nextFieldAccessDescriptor.getCurrentField();
            }
        }
    }

    public static FieldAccessDescriptor parse(String fieldPath) {
        if (cacheMap.containsKey(fieldPath)) {
            return cacheMap.get(fieldPath);
        } else {
            FieldAccessDescriptor fieldAccessDescriptor = new FieldAccessDescriptor(fieldPath);
            cacheMap.put(fieldPath, fieldAccessDescriptor);
            return fieldAccessDescriptor;
        }
    }
}
