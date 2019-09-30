package com.github.hui.media.console.util;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.util.FontUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yihui on 2017/9/22.
 */
public class DrawUtil {


    public static void drawSign(BufferedImage bufferedImage) {
        List<String> sign = Arrays.asList("by 图文小工具"); //
        // , ChineseDataExTool.getNowLunarDate(), " ---- 版权没有，随意转载 ---- ");
        drawSign(bufferedImage, sign);
    }

    public static void drawSign(BufferedImage bufferedImage, List<String> sign) {
        Graphics2D g2d = GraphicUtil.getG2d(bufferedImage);
        g2d.setColor(Color.GRAY);
        g2d.setFont(FontUtil.DEFAULT_FONT);


        FontMetrics fontMetrics = g2d.getFontMetrics();
        int fontSize = g2d.getFont().getSize();
        int signHeight = fontMetrics.getHeight() * sign.size() + fontSize;

        int offsetY = bufferedImage.getHeight() - signHeight;
        int offsetX = 0;
        int i = 0;
        for (String s: sign) {
            if (++i != sign.size()) {
                offsetX = bufferedImage.getWidth() - fontMetrics.stringWidth(s) - fontSize;
            } else {
                offsetX = (bufferedImage.getWidth() - fontMetrics.stringWidth(s)) >> 1;
            }
            g2d.drawString(s, offsetX, offsetY);
            offsetY += fontMetrics.getHeight();
        }
        g2d.dispose();
    }

}
