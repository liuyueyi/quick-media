package com.github.hui.quick.plugin.image.wrapper.pixel.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YiHui
 * @date 2022/7/8
 */
public class PixelUtil {
    /**
     * 求取多个颜色的平均值
     *
     * @param colors
     * @return
     */
    public static Color getAverage(List<Integer> colors) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int color : colors) {
            red += ((color & 0xff0000) >> 16);
            green += ((color & 0x00ff00) >> 8);
            blue += (color & 0x0000ff);
        }
        float size = colors.size();
        red = Math.round(red / size);
        green = Math.round(green / size);
        blue = Math.round(blue / size);
        return new Color(red, green, blue);
    }

    /**
     * 获取某一块的所有像素的颜色
     *
     * @param image
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public static List<Integer> getPixels(BufferedImage image, int x, int y, int w, int h) {
        List<Integer> colors = new ArrayList<>(w * h);
        for (int i = y; (i < h + y) && (i < image.getHeight()); i++) {
            for (int j = x; (j < w + x) && (j < image.getWidth()); j++) {
                int color = image.getRGB(j, i);
                colors.add(color);
            }
        }
        return colors;
    }
}
