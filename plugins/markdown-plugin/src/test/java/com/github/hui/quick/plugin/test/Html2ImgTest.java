package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.md.Html2ImageWrapper;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by @author yihui in 12:08 19/5/5.
 */
public class Html2ImgTest {

    @Test
    public void testHtmp2Img() throws Exception {
        String html = "<html>\n" + "<body>\n" + "\n" + "<span> hello world </span>\n" + "<hr/>\n" +
                "<button> 按钮 </button>\n" + "\n" + "</body>\n" + "</html>";
        BufferedImage img =
                Html2ImageWrapper.of(html).setAutoW(false).setAutoH(true).setOutType("jpg").build().asImage();
        System.out.println(img);
    }
}
