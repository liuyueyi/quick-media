package com.github.hui.quick.plugin.image.util;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by @author yihui in 19:04 18/12/8.
 */
public class StrUtil {
    private static String[] EMPTY_ARY = new String[0];

    public static String[] toArray(List<String> list) {
        if (list == null || list.size() == 0) {
            return EMPTY_ARY;
        }

        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    public static String replace(String str, String ...kv) {
        for (int i = 0; i < kv.length; i+=2) {
            str = StringUtils.replace(str, kv[i], kv[i+1]);
        }
        return str;
    }
}
