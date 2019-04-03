/**
 * Copyright(C) 2019 Hangzhou zhaoyunxing Technology Co., Ltd. All rights reserved.
 */
package io.github.xyz.spring.boot.netty.utils;

import java.util.UUID;

/**
 * @author zhaoyunxing
 * @date: 2019-04-03 15:53
 * @des: string 工具类
 */
public class StringUtils {
    private StringUtils() {
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isBlank(final CharSequence cs) {
        return isEmpty(cs);
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static String buildUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        return str.contains(searchStr);
    }

    public static boolean contains(String str, char searchChar) {
        if (isEmpty(str)) {
            return false;
        }
        return str.indexOf(searchChar) >= 0;
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

}
