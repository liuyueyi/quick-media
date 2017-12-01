package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.constants.MediaType;
import com.hust.hui.quickmedia.common.html.Html2ImageByJsWrapper;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.DomUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2017/12/1.
 */
public class Html2ImageByJsWrapperTest {

    @Test
    public void testRender() throws IOException {
        BufferedImage img = null;
        for (int i = 0; i < 20; ++i) {
            String url = "https://my.oschina.net/u/566591/blog/1580020";
            long start = System.currentTimeMillis();
            img = Html2ImageByJsWrapper.renderHtml2Image(url);
            long end = System.currentTimeMillis();
            System.out.println("cost:  " + (end - start));
        }

        System.out.println(DomUtil.toDomSrc(Base64Util.encode(img, "png"), MediaType.ImagePng));

    }

}
