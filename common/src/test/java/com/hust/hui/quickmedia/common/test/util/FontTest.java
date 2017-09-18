package com.hust.hui.quickmedia.common.test.util;

import com.hust.hui.quickmedia.common.util.GraphicUtil;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/8/29.
 */
public class FontTest {

    @Test
    public void testFontSize() {
        Font font = new Font("宋体", Font.BOLD, 18);


        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);

        FontMetrics fontMetrics = g2d.getFontMetrics();

        String[] msg = new String[]{"hello!", "12345!", "一二三四五!", ",.;'`!", "😄"};
        int width = 0;
        for (String m : msg) {
            width = fontMetrics.stringWidth(m);
            System.out.println(width);
        }

    }


    @Test
    public void testSplitStr() {
        int w = 300, h = 800;
        Font font = new Font("宋体", Font.BOLD, 18);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);


        g2d.setColor(Color.BLACK);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        String[] contents = new String[]{
                "这是一个两行的文本长度，划分一二三",
                "zheshiyigeyihangde changdu",
                "12345678901234567890"
        };


        int x = 60, y = 40;
        String[] ans;
        for (String str : contents) {
            ans = GraphicUtil.splitVerticalStr(str, 180, fontMetrics);
            for (String s : ans) {
                g2d.drawString(s, x, y);
                y += fontMetrics.getHeight();
            }
        }
        System.out.println("--over---");
    }


    @Test
    public void testDrawVerticle() {
        int w = 800, h = 400;
        Font font = new Font("宋体", Font.BOLD, 18);
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, w, h);


        g2d.setColor(Color.BLACK);

        FontMetrics fontMetrics = g2d.getFontMetrics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);


        String[] contents = new String[]{
                "这是一个两行的文本长度，划分一二三",
                "这是一个两行的文本长度，划分一二三",
                "这是一个两行的文本长度，划分一二三",
                "zheshiyigeyihangde changdu",
                "12345678901234567890"
        };


        // 垂直绘制
        int addX = 40;
        int addY = 40;
        int tempLen;
        int maxW = 0;
        for (String str : contents) {
            maxW = 0;
            addY = 40;
            tempLen = str.length();
            for (int i = 0; i < tempLen; i++) {
                maxW = Math.max(maxW, fontMetrics.charWidth(str.charAt(i)));
                g2d.drawString(str.charAt(i) + "", addX, addY);
                addY += fontMetrics.getHeight();
            }

            addX += maxW;
        }

        System.out.println("over");
    }

}
