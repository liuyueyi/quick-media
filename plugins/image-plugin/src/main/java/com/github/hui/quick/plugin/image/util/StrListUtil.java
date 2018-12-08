package com.github.hui.quick.plugin.image.util;

import java.util.List;

/**
 * Created by @author yihui in 19:04 18/12/8.
 */
public class StrListUtil {

    public static String[] toArray(List<String> list) {
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

}
