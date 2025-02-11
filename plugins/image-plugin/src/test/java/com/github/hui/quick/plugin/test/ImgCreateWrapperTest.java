package com.github.hui.quick.plugin.test;


import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.file.FileReadUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateWrapper;
import com.github.hui.quick.plugin.image.util.FontUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yihui on 2017/8/17.
 */
public class ImgCreateWrapperTest {

    @Test
    public void testGenImg() throws IOException {
        int w = 400;
        int leftPadding = 10;
        int topPadding = 40;
        int bottomPadding = 40;
        int linePadding = 10;
        Font font = new Font("宋体", Font.PLAIN, 18);

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setImgW(w)
                .setLeftPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(font)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL)
                .setBgImg(ImageLoadUtil.getImageByPath("createImg/bg.jpeg"))
                .setBgColor(Color.WHITE)
                .setBorder(true)
                .setBorderColor(0xFFF7EED6);


        BufferedReader reader = FileReadUtil.createLineRead("text/poem2.txt");
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);

            if (++index == 5) {
                build.drawImage(ImageLoadUtil.getImageByPath("https://static.oschina.net/uploads/img/201708/12175633_sOfz.png"));
            }

            if (index == 7) {
                build.setFontSize(25);
            }

            if (index == 10) {
                build.setFontSize(20);
                build.setFontColor(Color.RED);
            }
        }

        BufferedImage img = build.asImage();
        ImageIO.write(img, "png", new File("/tmp/more2out.png"));
        String out = Base64Util.encode(img, "png");
        System.out.println("<img src=\"data:image/png;base64," + out + "\" />");
    }


    @Test
    public void testLocalGenImg() throws IOException {
        int w = 400;
        int leftPadding = 10;
        int topPadding = 20;
        int bottomPadding = 10;
        int linePadding = 10;
        Font font = new Font("手札体", Font.PLAIN, 18); // 需要操作系统有手札字体，否则用默认的

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setImgW(w) // 设置图片宽
                .setLeftPadding(leftPadding) // 左边距
                .setRightPadding(leftPadding) // 右边距
                .setTopPadding(topPadding) // 上边距
                .setBottomPadding(bottomPadding) // 下边距
                .setLinePadding(linePadding) // 行间距
                .setFont(font) // 字体
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER) // 文字对齐方式
                .setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL) // 绘制样式，水平绘制
                .setBgColor(Color.WHITE) // 背景色为白色
                .setBorder(true) // 输出图片有边框
                .setBorderColor(0xFFF7EED6); // 边框颜色


        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt"); // 读取文字样本
        String line;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);
        }

        build.setAlignStyle(ImgCreateOptions.AlignStyle.RIGHT)
                .drawImage("https://spring.hhui.top/spring-blog/imgs/info/info.png");

        BufferedImage img = build.asImage();
        ImageIO.write(img, "png", new File("/tmp/2out.png"));
    }


    @Test
    public void testLocalGenVerticalImg() throws IOException, FontFormatException {
        int h = 400;
        int leftPadding = 10;
        int topPadding = 10;
        int bottomPadding = 10;
        int linePadding = 10;
        Font font = FontUtil.getFont("font/txlove.ttf", Font.PLAIN, 20);

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setImgH(h)
                .setDrawStyle(ImgCreateOptions.DrawStyle.VERTICAL_RIGHT)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .setLeftPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(font)
                .setBgColor(Color.WHITE)
                .setBorder(true)
                .setBorderBottomPadding(8)
                .setBorderLeftPadding(6)
                .setBorderTopPadding(8)
                .setBorderColor(0xFFF7EED6);


        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
        String line;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);
        }

        build.setFont(FontUtil.getFontOrDefault(null, Font.ITALIC, 18))
                .setAlignStyle(ImgCreateOptions.AlignStyle.BOTTOM);
        build.drawContent(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        build.drawContent(" ");
        build.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .drawImage("https://spring.hhui.top/spring-blog/imgs/info/info.png");
        build.setFontColor(Color.BLUE).drawContent("后缀签名").drawContent("一灰灰");

        BufferedImage img = build.asImage();
        ImageIO.write(img, "png", new File("/tmp/v2out.png"));
    }
}
