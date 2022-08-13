package com.github.hui.quick.plugin.qrcode.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class JacksonUtil {
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    static <T> T toObj(String str, Class<T> clz) {
        try {
            return jsonMapper.readValue(str, clz);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    static <T> String toStr(T t) {
        try {
            return jsonMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
