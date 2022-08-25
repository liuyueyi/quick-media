package com.github.hui.quick.plugin.qrcode.util.json;

import com.google.gson.Gson;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class GsonImpl implements JsonApi {
    private static final Gson gson = new Gson();

    public <T> T toObj(String str, Class<T> t) {
        return gson.fromJson(str, t);
    }

    public <T> String toStr(T t) {
        return gson.toJson(t);
    }
}
