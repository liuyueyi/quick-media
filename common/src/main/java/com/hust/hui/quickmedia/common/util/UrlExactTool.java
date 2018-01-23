package com.hust.hui.quickmedia.common.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yihui on 2017/12/8.
 */
public class UrlExactTool {
    /**
     * 从url中提取域名
     * <p/>
     * todo 考虑正则提取 : (http://|https://)?[0-9a-zA-Z-_\.]+(:[0-9]+)?(/.*)
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        int first = url.startsWith("http") ? url.indexOf(':') + 3 : 0;


        // 参数的索引
        int paramIndex = url.indexOf("?");
        if(paramIndex < 0) { // 没有url参数
            paramIndex = url.length();
        }

        // path的索引
        int pathIndex = url.indexOf('/', first);
        if(pathIndex < 0) { // 没有path
            pathIndex = url.length();
        }


        // 域名中的端口号

        String domain = url.substring(first, Math.min(paramIndex, pathIndex));

        int portIndex = domain.indexOf(":", first);
        if (portIndex < 0) {
            return domain;
        } else {
            return domain.substring(0, portIndex);
        }
    }


    private static final Pattern FIRST_DOMAIN_PATTERN = Pattern.compile("([0-9a-zA-Z\\-_]+\\.)*([0-9a-zA-Z\\-_]+\\.[0-9a-zA-Z\\-_]+)");

    /**
     * 获取一级域名
     * <p>
     * eg: www.zbang.online  返回   zbang.online
     * eg: www.blog.zbang.online 返回 zbang.online
     * eg: local.b_new.z-bang.online 返回 z-bang.online
     * eg: http://www.test.baidu.com?z-bang.online 返回 baidu.com
     *
     * @param domain
     * @return
     */
    public static String getRootDomain(String domain) {
        if (StringUtils.isBlank(domain)) {
            return null;
        }

        Matcher matcher = FIRST_DOMAIN_PATTERN.matcher(domain);
        if (matcher.find()) {
            if (matcher.groupCount() > 1) {
                return matcher.group(2);
            } else {
                return matcher.group(1);
            }
        }

        return null;
    }
}
