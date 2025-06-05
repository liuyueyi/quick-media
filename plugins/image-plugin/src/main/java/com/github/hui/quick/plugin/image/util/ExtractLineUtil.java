package com.github.hui.quick.plugin.image.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片线图提取工具类
 *
 * @author YiHui
 * @date 2025/6/5
 */
public class ExtractLineUtil {

    public static BufferedImage extractLineDrawing(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // 创建灰度图像数组
        double[] imgray = new double[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(originalImage.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                // 计算灰度值
                double Y = 0.3 * r + 0.59 * g + 0.11 * b;
                imgray[y * width + x] = Y;
            }
        }

        // 计算局部最小值的补值
        double[] mina = new double[width * height];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int u = Math.max(0, h - 1);
                int d = Math.min(height - 1, h + 1);
                int l = Math.max(0, w - 1);
                int r = Math.min(width - 1, w + 1);

                double maxNeighbor = Math.max(
                        Math.max(imgray[u * width + l], Math.max(imgray[u * width + w], imgray[u * width + r])),
                        Math.max(
                                Math.max(imgray[h * width + l], Math.max(imgray[h * width + w], imgray[h * width + r])),
                                Math.max(imgray[d * width + l], Math.max(imgray[d * width + w], imgray[d * width + r]))
                        )
                );
                mina[h * width + w] = 255 - maxNeighbor;
            }
        }

        // 计算最终结果
        double[] result = new double[width * height];
        for (int i = 0; i < imgray.length; i++) {
            double Y = imgray[i];
            double M = mina[i];
            result[i] = Y + (Y * M) / (255 - M);
        }

        // 创建处理后的图像
        BufferedImage processedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = (int) result[y * width + x];
                value = Math.max(0, Math.min(255, value));
                Color color = new Color(value, value, value);
                processedImage.setRGB(x, y, color.getRGB());
            }
        }

        return processedImage;
    }
}
