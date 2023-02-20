package com.itheima.pinda.common.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author: zhangdongjiang
 * @date: 2019-06-06 11:04
 */
public class StringGenius {
    /**
     * 左对齐(右侧补位)
     *
     * @param src
     * @param len
     * @param ch
     * @return
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * 右对齐(左侧补位)
     *
     * @param src
     * @param len
     * @param ch
     * @return
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

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
