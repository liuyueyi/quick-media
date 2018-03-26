package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.md.Html2ImageWrapper;
import com.github.hui.quick.plugin.md.MarkDown2HtmlWrapper;
import com.github.hui.quick.plugin.md.entity.MarkdownEntity;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

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
        ImageIO.write(bf, "jph", new File("test_out.jpg"));
        System.out.println("---over---");
    }

}
