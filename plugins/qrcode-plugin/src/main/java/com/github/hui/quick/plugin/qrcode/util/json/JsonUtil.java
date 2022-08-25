package com.github.hui.quick.plugin.qrcode.util.json;

import com.github.hui.quick.plugin.qrcode.util.ClassUtils;

import java.util.ServiceLoader;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class JsonUtil {
    private static JsonApi jsonApi;

private static void initJsonApi() {
    if (jsonApi == null) {
        synchronized (JsonUtil.class) {
            if (jsonApi == null) {
                ServiceLoader<JsonApi> loader = ServiceLoader.load(JsonApi.class);
                for (JsonApi value : loader) {
                    jsonApi = value;
                    return;
                }

                if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", JsonUtil.class.getClassLoader())) {
                    jsonApi = new JacksonImpl();
                } else if (ClassUtils.isPresent("com.google.gson.Gson", JsonUtil.class.getClassLoader())) {
                    jsonApi = new GsonImpl();
                } else if (ClassUtils.isPresent("com.alibaba.fastjson.JSONObject", JsonUtil.class.getClassLoader())) {
                    jsonApi = new JacksonImpl();
                } else{
                    throw new UnsupportedOperationException("no json framework to deserialize string! please import jackson|gson|fastjson");
                }
            }
        }
    }
}

    /**
     * json转实体类，会根据当前已有的json框架来执行反序列化
     *
     * @param str
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T toObj(String str, Class<T> t) {
        initJsonApi();
        return jsonApi.toObj(str, t);
    }

    public static <T> String toStr(T t) {
        initJsonApi();
        return jsonApi.toStr(t);
    }
}
