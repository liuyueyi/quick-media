package com.github.hui.quick.plugin.qrcode.util.json;

import com.alibaba.fastjson.JSONObject;

/**
 * @author YiHui
 * @date 2022/8/13
 */
public class FastjsonImpl implements JsonApi {
    public <T> T toObj(String str, Class<T> clz) {
        return JSONObject.parseObject(str, clz);
    }

    public <T> String toStr(T t) {
        return JSONObject.toJSONString(t);
    }
}
