package com.github.hui.media.console.entity;

/**
 * Created by yihui on 2017/12/29.
 */
public enum OutputEnum {
    STREAM, // 表示直接返回图片
    URL, // 返回url地址
    IMG; // 返回base64的图片

    public static OutputEnum ofStr(String output) {
        if (STREAM.name().equalsIgnoreCase(output)) {
            return STREAM;
        } else if (IMG.name().equalsIgnoreCase(output)) {
            return IMG;
        } else {
            return URL;
        }
    }
}
