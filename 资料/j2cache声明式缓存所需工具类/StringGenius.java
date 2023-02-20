package com.itheima.j2cache.utils;
import org.springframework.util.StringUtils;
import java.util.regex.Pattern;
/**
 * 字符串工具类
 */
public class StringGenius {
    /**
     * 是否是整数
     *
     * @param text 文本
     * @return true：是；false：否
     */
    public static boolean isInteger(String text) {
        if (!StringUtils.hasText(text)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(text).matches();
    }
}
