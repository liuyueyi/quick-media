package com.github.hui.quick.plugin.image.helper;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片像素画处理
 *
 * @author yihui
 * @data 2021/11/7
 */
public class ImgPixelHelper {
    static String ascii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'.";

    /**
     * 基于颜色的灰度值，获取对应的字符
     * @param g
     * @return
     */
    public static char toChar(Color g) {
        double gray = 0.299 * g.getRed() + 0.578 * g.getGreen() + 0.114 * g.getBlue();
        return ascii.charAt((int) (gray / 255 * ascii.length()));
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
    public static int[] getPixels(BufferedImage image, int x, int y, int w, int h) {
        int[] colors = new int[w * h];
        int idx = 0;
        for (int i = y; (i < h + y) && (i < image.getHeight()); i++) {
            for (int j = x; (j < w + x) && (j < image.getWidth()); j++) {
                int color = image.getRGB(j, i);
                colors[idx++] = color;
            }
        }
        return colors;
    }
}
