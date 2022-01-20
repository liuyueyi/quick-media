package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.md.Html2ImageWrapper;
import com.github.hui.quick.plugin.md.MarkDown2HtmlWrapper;
import com.github.hui.quick.plugin.md.entity.MarkdownEntity;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by yihui on 2018/3/26.
 */
public class Md2ImgTest {

    @Test
    public void testMd2Img() throws Exception {
        MarkdownEntity entity = MarkDown2HtmlWrapper.ofFile("md/test.md");
        BufferedImage bf = Html2ImageWrapper.ofMd(entity)
                .setW(800)
                .setAutoW(false)
                .setAutoH(true)
                .setOutType("jpg")
                .build()
                .asImage();
        ImageIO.write(bf, "jpg", new File("md_out.jpg"));
        System.out.println("---over---");
    }


    @Test
    public void testMd2Img2() throws Exception {
        MarkdownEntity entity = MarkDown2HtmlWrapper.ofFile("md/test.md");
        entity.setCss(MarkDown2HtmlWrapper.buildCssContent("md/heyrain.css"));

        File file = new File("out.html");
        FileOutputStream out = new FileOutputStream(file);
        out.write(entity.toString().getBytes());
        out.flush();
        out.close();

        BufferedImage bf = Html2ImageWrapper.ofMd(entity)
                .setW(500)
                .setAutoW(false)
                .setAutoH(true)
                .setOutType("jpg")
                /*
                在其他平台最好设置一个通用字体, 否则可能会出现由于字体字库不全而导致的乱码问题
                如在win下设置为'微软雅黑'
                .setFontFamily("微软雅黑")
                */
                .build()
                .asImage();
        ImageIO.write(bf, "jpg", new File("md_out_2.jpg"));
        System.out.println("---over---");
    }
}
