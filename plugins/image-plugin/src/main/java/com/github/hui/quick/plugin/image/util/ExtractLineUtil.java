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

    /**
     * 基于简单算法对比的线稿提取
     *
     * @param originalImage
     * @return
     */
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


    /**
     * 基于边缘检测的线图提取
     *
     * @return
     */
    public static BufferedImage extractLineBySobelDetect(BufferedImage sourceImage) {
        // 1. 将图片转换为灰度图
        BufferedImage grayImage = toGray(sourceImage);

        // 2. 应用高斯模糊
        BufferedImage blurredImage = applyGaussianBlur(grayImage, 3);

        // 3. 二值化
//        BufferedImage binImage = binarize(blurredImage, 150);
//        BufferedImage lineArtImage = sobelEdgeDetection(blurredImage, 30);

        // 4. 应用Sobel算子进行边缘检测，并进行颜色反转
        BufferedImage lineArtImage = sobelEdgeDetection(blurredImage, 128);

        return lineArtImage;
    }


    /**
     * 将彩色图转换为灰度图
     *
     * @param image 原始彩色 BufferedImage
     * @return 灰度 BufferedImage
     */
    public static BufferedImage toGray(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                // 使用加权平均法进行灰度化
                int gray = (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
                grayImage.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        return grayImage;
    }

    /**
     * 应用高斯模糊
     *
     * @param image 灰度图
     * @return 模糊后的图像
     */
    public static BufferedImage applyGaussianBlur(BufferedImage image, int kernelSize) {
        // 定义一个简单的 3x3 高斯模糊核
        float[] kernel = {
                1 / 16f, 2 / 16f, 1 / 16f,
                2 / 16f, 4 / 16f, 2 / 16f,
                1 / 16f, 2 / 16f, 1 / 16f
        };
        return applyConvolution(image, kernel, kernelSize);
    }


    /**
     * 应用卷积操作（一个通用的辅助方法）
     *
     * @param image      输入图像
     * @param kernel     卷积核
     * @param kernelSize 卷积核大小 (例如 3x3 的 size 为 3)
     * @return 卷积后的图像
     */
    private static BufferedImage applyConvolution(BufferedImage image, float[] kernel, int kernelSize) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        int width = image.getWidth();
        int height = image.getHeight();
        int kernelRadius = kernelSize / 2;

        for (int y = kernelRadius; y < height - kernelRadius; y++) {
            for (int x = kernelRadius; x < width - kernelRadius; x++) {
                float sum = 0;
                int kernelIndex = 0;
                for (int ky = -kernelRadius; ky <= kernelRadius; ky++) {
                    for (int kx = -kernelRadius; kx <= kernelRadius; kx++) {
                        int pixel = new Color(image.getRGB(x + kx, y + ky)).getRed();
                        sum += pixel * kernel[kernelIndex++];
                    }
                }
                int newPixelValue = Math.min(255, Math.max(0, (int) sum));
                resultImage.setRGB(x, y, new Color(newPixelValue, newPixelValue, newPixelValue).getRGB());
            }
        }
        return resultImage;
    }

    public static BufferedImage binarize(BufferedImage image, int threshold) {
        BufferedImage binaryImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int gray = new Color(image.getRGB(x, y)).getRed();
                if (gray < threshold) {
                    binaryImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    binaryImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        return binaryImage;
    }

    /**
     * 应用 Sobel 边缘检测
     *
     * @param image 经过高斯模糊的灰度图
     * @return 最终的线稿图
     */
    public static BufferedImage sobelEdgeDetection(BufferedImage image, int threshold) {
        BufferedImage resultImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        // Sobel 算子
        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;

                // 应用 Sobel 算子
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int gray = new Color(image.getRGB(x + i, y + j)).getRed();
                        gx += gray * sobelX[i + 1][j + 1];
                        gy += gray * sobelY[i + 1][j + 1];
                    }
                }

                // 计算梯度幅度
                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);

                // 阈值处理和颜色反转
                // 如果梯度大于阈值，则认为是边缘，画成黑色 (0)
                // 否则认为是背景，画成白色 (255)
                if (magnitude > threshold) {
                    resultImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    resultImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        return resultImage;
    }

}
