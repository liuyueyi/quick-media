package com.github.hui.quick.plugin.image.helper;

import com.github.hui.quick.plugin.base.GraphicUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片像素画处理
 *
 * @author yihui
 * @data 2021/11/7
 */
public class ImgPixelHelper {

    /**
     * 转成像素图片
     *
     * @param bufferedImage 原图片
     * @param blockSize     块大小
     * @return
     */
    public static BufferedImage getBlockBitmap(BufferedImage bufferedImage, int blockSize) {
        if (bufferedImage == null)
            throw new IllegalArgumentException("Bitmap cannot be null.");
        int picWidth = bufferedImage.getWidth();
        int picHeight = bufferedImage.getHeight();

        BufferedImage back = new BufferedImage(picWidth / blockSize * blockSize, picHeight / blockSize * blockSize, bufferedImage.getType());
        Graphics2D g2d = GraphicUtil.getG2d(back);
        g2d.setColor(Color.WHITE);
        for (int y = 0; y < picHeight; y += blockSize) {
            for (int x = 0; x < picWidth; x += blockSize) {
                int[] colors = getPixels(bufferedImage, x, y, blockSize, blockSize);
                g2d.setColor(getAverage(colors));
                g2d.fillRect(x, y, blockSize, blockSize);
            }
        }

        return back;
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
    private static int[] getPixels(BufferedImage image, int x, int y, int w, int h) {
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

    /**
     * 求取多个颜色的平均值
     *
     * @param colors
     * @return
     */
    private static Color getAverage(int[] colors) {
        //int alpha=0;
        int red = 0;
        int green = 0;
        int blue = 0;
        for (int color : colors) {
            red += ((color & 0xff0000) >> 16);
            green += ((color & 0xff00) >> 8);
            blue += (color & 0x0000ff);
        }
        float len = colors.length;
        //alpha=Math.round(alpha/len);
        red = Math.round(red / len);
        green = Math.round(green / len);
        blue = Math.round(blue / len);
        return new Color(0xff, red, green, blue);
    }
}
