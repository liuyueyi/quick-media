package com.github.hui.quick.plugin.qrcode.util.json;

import com.google.gson.Gson;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class GsonUtil {
    private static final Gson gson = new Gson();

    static <T> T toObj(String str, Class<T> t) {
        return gson.fromJson(str, t);
    }

    static <T> String toStr(T t) {
        return gson.toJson(t);
    }
}
