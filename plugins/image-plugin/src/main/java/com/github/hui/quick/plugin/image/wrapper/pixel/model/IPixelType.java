package com.github.hui.quick.plugin.image.wrapper.pixel.model;

import java.awt.*;

/**
 * @author yihui
 * @date 21/11/8
 */
public interface IPixelType {
    /**
     * 求取多个颜色的平均值
     *
     * @param colors
     * @return
     */
    default Color getAverage(int[] colors) {
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int color : colors) {
            red += ((color & 0xff0000) >> 16);
            green += ((color & 0xff00) >> 8);
            blue += (color & 0x0000ff);
        }
        int len = colors.length;
        return calculateColor(red, green, blue, len);
    }

    /**
     * 计算颜色均值
     *
     * @param red
     * @param green
     * @param blue
     * @param size
     * @return
     */
    Color calculateColor(int red, int green, int blue, int size);

    /**
     * 图片绘制
     *
     * @param g2d
     * @param x
     * @param y
     * @param width
     * @param height
     */
    default void draw(Graphics2D g2d, Color color, int x, int y, int width, int height) {
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
    }
}
