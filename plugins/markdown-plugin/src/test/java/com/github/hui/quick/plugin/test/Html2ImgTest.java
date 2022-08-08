package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.md.Html2ImageWrapper;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by @author yihui in 12:08 19/5/5.
 */
public class Html2ImgTest {

    @Test
    public void testHtmp2Img() throws Exception {
        String html =
                "<html>\n" + "<body>\n" + "\n" + "<span> hello world 爱喝酒</br> 测试测试礼金卡打扫房间连咖啡 </span>\n" + "<hr/>\n" +
                        "<button> 按钮 </button>\n" + "\n" + "</body>\n" + "</html>";
        BufferedImage img =
                Html2ImageWrapper.of(html).setFontColor(0xFFFF0000).setAutoW(false).setAutoH(true).setOutType("jpg")
                        .setFontFamily("monospace").build().asImage();
        System.out.println(img);
    }

    /**
     * css 文件和html文件分开的情况
     *
     * @throws Exception
     */
    @Test
    public void testParseHtml2Img() throws Exception {
        String html = FileReadUtil.readAll("html/demo.html");

        String MD_CSS = FileReadUtil.readAll("html/demo.css");
        MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";

        BufferedImage img =
                Html2ImageWrapper.of(html).setCss(MD_CSS).setAutoW(false).setAutoH(false).setOutType("jpg").build()
                        .asImage();
        System.out.println(img);
    }


    /**
     * 完整的html文档, 希望直接进行渲染输出图片，使用 ofDoc 方式
     *
     * @throws Exception
     */
    @Test
    public void testFullHtml() throws Exception {
        String html = FileReadUtil.readAll("html/full.html");
        BufferedImage img = Html2ImageWrapper.ofDoc(html).setOutType("jpg").build().asImage();
        System.out.println(img);
    }
}
