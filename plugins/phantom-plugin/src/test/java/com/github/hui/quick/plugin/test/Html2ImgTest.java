package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.phantom.Html2ImageByJsWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2018/3/26.
 */
public class Html2ImgTest {

    @Test
    public void testRender() throws IOException {
        try {
            BufferedImage img = null;
            for (int i = 0; i < 20; ++i) {
                String url = "https://my.oschina.net/u/566591/blog/1580020";
                long start = System.currentTimeMillis();
                img = Html2ImageByJsWrapper.renderHtml2Image(url);
                long end = System.currentTimeMillis();
                System.out.println("cost:  " + (end - start));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
