package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.image.ImgCreateOptions;
import com.hust.hui.quickmedia.common.image.ImgCreateWrapper;
import com.hust.hui.quickmedia.common.tools.ChineseDataExTool;
import com.hust.hui.quickmedia.common.util.Base64Util;
import com.hust.hui.quickmedia.common.util.FileReadUtil;
import com.hust.hui.quickmedia.common.util.FontUtil;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

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
//                .setBgImg(ImageUtil.getImageByPath("createImg/bg.jpeg"))
                .setBgColor(Color.WHITE)
                .setBorder(true)
                .setBorderColor(0xFFF7EED6);


        BufferedReader reader = FileReadUtil.createLineRead("text/poem2.txt");
        String line;
        int index = 0;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);

            if (++index == 5) {
                build.drawImage(ImageUtil.getImageByPath("https://static.oschina.net/uploads/img/201708/12175633_sOfz.png"));
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
        Font font = new Font("手札体", Font.PLAIN, 18);

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setImgW(w)
                .setLeftPadding(leftPadding)
                .setRightPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(font)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .setBgColor(Color.WHITE)
                .setBorder(true)
                .setBorderColor(0xFFF7EED6);


        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
        String line;
        while ((line = reader.readLine()) != null) {
            build.drawContent(line);
        }

        build.setAlignStyle(ImgCreateOptions.AlignStyle.RIGHT)
                .drawImage("/Users/yihui/Desktop/sina_out.jpg");

        BufferedImage img = build.asImage();
        ImageIO.write(img, "png", new File("/Users/yihui/Desktop/2out.png"));
    }


    @Test
    public void testLocalGenVerticalImg() throws IOException, FontFormatException {
        int h = 200;
        int leftPadding = 10;
        int topPadding = 10;
        int bottomPadding = 10;
        int linePadding = 10;
//        Font font = new Font("楷体", Font.PLAIN, 18);
        Font font = FontUtil.getFont("font/txlove.ttf", Font.PLAIN, 12);

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setImgH(h)
                .setDrawStyle(ImgCreateOptions.DrawStyle.VERTICAL_RIGHT)
                .setLeftPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(font)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
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

        build.setFont(FontUtil.getFont("font/txlove.ttf", Font.ITALIC, 8))
                .setAlignStyle(ImgCreateOptions.AlignStyle.BOTTOM);
        build.drawContent(ChineseDataExTool.getNowLunarDate());
        build.drawContent(" ");
        build.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .drawImage("/Users/yihui/Desktop/long_out.png");
//        build.setFontColor(Color.BLUE).drawContent("后缀签名").drawContent("灰灰自动生成");

        BufferedImage img = build.asImage();
        ImageIO.write(img, "png", new File("/Users/yihui/Desktop/2out.png"));
    }


    @Test
    public void testWxImg() throws IOException, FontFormatException {
        int h = 500;
        int leftPadding = 10;
        int topPadding = 10;
        int bottomPadding = 10;
        int linePadding = 10;
//        Font font = new Font("楷体", Font.PLAIN, 18);
        Font font = FontUtil.getFont("font/txlove.ttf", Font.PLAIN, 38);

        ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                .setImgW(h)
                .setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL)
                .setLeftPadding(leftPadding)
                .setRightPadding(leftPadding)
                .setTopPadding(topPadding)
                .setBottomPadding(bottomPadding)
                .setLinePadding(linePadding)
                .setFont(font)
                .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .setBgColor(Color.WHITE)
                .setBorder(true)
                .setBorderColor(0xFFF7EED6);

        build.drawContent("ForkJoin 学习使用笔记");
        build.setFont(font.deriveFont(Font.PLAIN, 18))
                .setFontColor(Color.BLUE)
                .setAlignStyle(ImgCreateOptions.AlignStyle.RIGHT)
                .drawContent("—— 一灰");
        build.setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                .drawImage("/Users/yihui/Desktop/wx.jpg");
        BufferedImage img = build.asImage();
        ImageIO.write(img, "png", new File("/Users/yihui/Desktop/3out.png"));
    }
}
