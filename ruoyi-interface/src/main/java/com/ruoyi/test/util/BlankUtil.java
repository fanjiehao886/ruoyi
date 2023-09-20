package com.ruoyi.test.util;

public class BlankUtil {
    public static boolean isBlank(final String str) {
        return(str == null) || (str.trim().length() <= 0);
    }
}
