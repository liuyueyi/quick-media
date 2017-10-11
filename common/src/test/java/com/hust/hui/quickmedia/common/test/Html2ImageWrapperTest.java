package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.html.Html2ImageWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkDown2HtmlWrapper;
import com.hust.hui.quickmedia.common.markdown.MarkdownEntity;
import com.hust.hui.quickmedia.common.util.DrawUtil;
import com.hust.hui.quickmedia.common.util.HttpUtil;
import com.hust.hui.quickmedia.common.util.NumUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yihui on 2017/9/11.
 */
public class Html2ImageWrapperTest {

    @Test
    public void testParse() throws Exception {

        String file = "md/test.md";

        MarkdownEntity html = MarkDown2HtmlWrapper.ofFile(file);

        BufferedImage img = Html2ImageWrapper.ofMd(html)
                .setW(800)
                .setAutoW(false)
                .setAutoH(true)
                .setOutType("jpg")
                .build()
                .asImage();

        // 添加签名
        DrawUtil.drawSign(img);

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

    public String inputStream2String(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    @Test
    public void html2image() throws Exception {
        String html = "http://blog.csdn.net/u011240877/article/details/53358305";
        InputStream stream = HttpUtil.downFile(html);
        String content = inputStream2String(stream);
        BufferedImage img = Html2ImageWrapper.of(content)
                .setW(800)
                .setAutoW(false)
                .setAutoH(true)
                .setOutType("jpg")
                .build()
                .asImage();

        // 添加签名
        DrawUtil.drawSign(img);

        ImageIO.write(img, "jpg", new File("/Users/yihui/Desktop/md2.jpg"));
    }
}
