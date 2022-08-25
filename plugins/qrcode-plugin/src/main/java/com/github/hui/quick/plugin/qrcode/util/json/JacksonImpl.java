package com.github.hui.quick.plugin.qrcode.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class JacksonImpl implements JsonApi{
    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public <T> T toObj(String str, Class<T> clz) {
        try {
            return jsonMapper.readValue(str, clz);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public <T> String toStr(T t) {
        try {
            return jsonMapper.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
