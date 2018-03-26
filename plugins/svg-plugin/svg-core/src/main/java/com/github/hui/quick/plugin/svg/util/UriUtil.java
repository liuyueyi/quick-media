package com.github.hui.quick.plugin.svg.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by yihui on 2018/1/14.
 */
public class UriUtil {

    public static String getAbsUri(String url) throws URISyntaxException {
        URI uri;
        if (url.startsWith("http")) { // 网络路径
            uri = URI.create(url);
        } else if(url.startsWith("/")) { // 绝对路径
            uri = URI.create(url);
        } else { // 相对路径
            uri = UriUtil.class.getClassLoader().getResource(url).toURI();
        }
        return uri.toString();
    }

}
