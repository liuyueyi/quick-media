package com.github.hui.quick.plugin.photo.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author YiHui
 * @date 2025/1/24
 */
public class GaussianFilter {
    private static final double[][] GAUSSIAN_KERNEL = {
            {1 / 16.0, 1 / 8.0, 1 / 16.0},
            {1 / 8.0, 1 / 4.0, 1 / 8.0},
            {1 / 16.0, 1 / 8.0, 1 / 16.0}
    };

    public static BufferedImage applyGaussianFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int kernelSize = GAUSSIAN_KERNEL.length;
        int offset = kernelSize / 2;

        for (int x = offset; x < width - offset; x++) {
            for (int y = offset; y < height - offset; y++) {
                double sumRed = 0;
                double sumGreen = 0;
                double sumBlue = 0;

                for (int i = -offset; i <= offset; i++) {
                    for (int j = -offset; j <= offset; j++) {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        double kernelValue = GAUSSIAN_KERNEL[i + offset][j + offset];
                        sumRed += color.getRed() * kernelValue;
                        sumGreen += color.getGreen() * kernelValue;
                        sumBlue += color.getBlue() * kernelValue;
                    }
                }

                int avgRed = (int) Math.min(255, Math.max(0, sumRed));
                int avgGreen = (int) Math.min(255, Math.max(0, sumGreen));
                int avgBlue = (int) Math.min(255, Math.max(0, sumBlue));

                filteredImage.setRGB(x, y, new Color(avgRed, avgGreen, avgBlue).getRGB());
            }
        }


        return filteredImage;
    }
}
