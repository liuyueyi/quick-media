package com.github.hui.quick.plugin.qrcode.util.json;

/**
 * @author YiHui
 * @date 2022/8/24
 */
public interface JsonApi {
    <T> T toObj(String str, Class<T> clz);

    <T> String toStr(T t);
}
