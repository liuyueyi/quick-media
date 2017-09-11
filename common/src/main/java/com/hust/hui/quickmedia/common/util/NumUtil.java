package com.hust.hui.quickmedia.common.util;

/**
 * Created by yihui on 2017/7/18.
 */
public class NumUtil {
    /**
     * 将(十进制,八进制,十六进制) 字符串格式数字,转换为int
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static Integer decode2int(String str, Integer defaultValue) {
        if (str == null) {
            return defaultValue;
        }

        try {
            //xxx 用Long转,可以避免 0xffee0011 这种解析的逸出 Integer.decode 只能转整数,负数会报错
            return Long.decode(str).intValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public static String toHex(int hex) {
        String str = Integer.toHexString(hex);
        if (str.length() == 1) {
            return "0" + str;
        }

        return str;
    }
}
