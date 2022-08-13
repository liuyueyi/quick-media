package com.github.hui.quick.plugin.qrcode.util.json;

import com.github.hui.quick.plugin.qrcode.util.ClassUtils;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class JsonUtil {
    /**
     * json转实体类，会根据当前已有的json框架来执行反序列化
     *
     * @param str
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T toObj(String str, Class<T> t) {
        if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", JsonUtil.class.getClassLoader())) {
            return JacksonUtil.toObj(str, t);
        } else if (ClassUtils.isPresent("com.google.gson.Gson", JsonUtil.class.getClassLoader())) {
            return GsonUtil.toObj(str, t);
        } else if (ClassUtils.isPresent("com.alibaba.fastjson.JSONObject", JsonUtil.class.getClassLoader())) {
            return FastjsonUtil.toObj(str, t);
        } else {
            throw new UnsupportedOperationException("no json framework to deserialize string! please import jackson|gson|fastjson");
        }
    }

    public static <T> String toStr(T t) {
        if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", JsonUtil.class.getClassLoader())) {
            return JacksonUtil.toStr(t);
        } else if (ClassUtils.isPresent("com.google.gson.Gson", JsonUtil.class.getClassLoader())) {
            return GsonUtil.toStr(t);
        } else if (ClassUtils.isPresent("com.alibaba.fastjson.JSONObject", JsonUtil.class.getClassLoader())) {
            return FastjsonUtil.toStr(t);
        } else {
            throw new UnsupportedOperationException("no json framework to deserialize string! please import jackson|gson|fastjson");
        }
    }

}
