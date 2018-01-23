package com.hust.hui.quickmedia.common.test.util;

import com.hust.hui.quickmedia.common.util.UrlExactTool;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yihui on 2017/12/8.
 */
@Slf4j
public class UrlExactTest {

    @Test
    public void testGetDomain() {
        String url = "http://www.zbang.online?param=hello";
        String ans = UrlExactTool.getDomain(url);
        Assert.assertEquals(ans, "www.zbang.online");


        url = "https://www.123.zbang.online";
        ans = UrlExactTool.getDomain(url);
        Assert.assertEquals(ans, "www.123.zbang.online");

        url = "www.zbang.online";
        ans = UrlExactTool.getDomain(url);
        Assert.assertEquals(ans, "www.zbang.online");


        url = "https://www.zbang.online:8080";
        ans = UrlExactTool.getDomain(url);
        Assert.assertEquals(ans, "www.zbang.online");


        url = "https://www.zbang.online?www.baidu.com";
        ans = UrlExactTool.getDomain(url);
        Assert.assertEquals(ans, "www.zbang.online");


        url = "https://123.zbang.online";
        ans = UrlExactTool.getDomain(url);
        Assert.assertEquals(ans, "123.zbang.online");

        url = "https://www.zbang.online&123?hello=123";
        ans = UrlExactTool.getDomain(url);
        Assert.assertEquals(ans, "www.zbang.online&123");
    }



    @Test
    public void testGetRootDomain() {
        String url = "http://www.zbang.online?param=hello";
        String ans = UrlExactTool.getRootDomain(url);
        Assert.assertEquals(ans, "zbang.online");


        url = "https://www.123.zbang.online";
        ans = UrlExactTool.getRootDomain(url);
        Assert.assertEquals(ans, "zbang.online");

        url = "www.zbang.online";
        ans = UrlExactTool.getRootDomain(url);
        Assert.assertEquals(ans, "zbang.online");


        url = "https://www.zbang.online:8080";
        ans = UrlExactTool.getRootDomain(url);
        Assert.assertEquals(ans, "zbang.online");


        url = "https://www.zbang.online?www.baidu.com";
        ans = UrlExactTool.getRootDomain(url);
        Assert.assertEquals(ans, "zbang.online");


        url = "https://123.zbang.online";
        ans = UrlExactTool.getRootDomain(url);
        Assert.assertEquals(ans, "zbang.online");

        url = "https://www.zbang.online&123?hello=123";
        ans = UrlExactTool.getRootDomain(url);
        Assert.assertEquals(ans, "zbang.online");
    }

}
