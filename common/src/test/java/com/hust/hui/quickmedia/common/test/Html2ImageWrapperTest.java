package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.html.Html2ImageWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkDown2HtmlWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkdownEntity;
import com.hust.hui.quickmedia.common.util.NumUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yihui on 2017/9/11.
 */
public class Html2ImageWrapperTest {

    @Test
    public void testParse() throws Exception {

        String file = "/Users/yihui/GitHub/Quick/quick-media/doc/images/imgGenV2.md";
//        String file = "md/tutorial.md";

        MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file);

        BufferedImage img = Html2ImageWrapper.ofMd(html)
                .setW(600)
                .setAutoW(false)
                .setAutoH(true)
                .setOutType("jpg")
                .build()
                .asImage();

        ImageIO.write(img, "jpg", new File("/Users/yihui/Desktop/md.jpg"));
    }


    @Test
    public void testFormat() {
        String TAG_WIDTH = "<style type=\"text/css\"> %s { width:85%%}  </style>";
        System.out.println(String.format(TAG_WIDTH, "img"));
    }


    @Test
    public void testToHex() {
        int num = 10;
        System.out.println(NumUtil.toHex(num));

        System.out.println(NumUtil.toHex(1));

        System.out.println(NumUtil.toHex(83));
    }


    @Test
    public void markdown2html() throws IOException {
        String file = "md/tutorial.md";
        MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file);
        System.out.println(html.toString());
    }
}
