package com.hust.hui.quickmedia.common.util;

import com.hust.hui.quickmedia.common.constants.MediaType;

/**
 * 多媒体前缀方式: http://www.cnblogs.com/del/archive/2012/03/14/2395782.html
 *
 * Created by yihui on 2017/8/18.
 */
public class DomUtil {


    /**
     * 将base64数据封装为html对应的dom属性值
     *
     * @param base64str
     * @return
     */
    public static String toDomSrc(String base64str, MediaType mediaType) {
        return  mediaType.getPrefix() + base64str;
    }
}
