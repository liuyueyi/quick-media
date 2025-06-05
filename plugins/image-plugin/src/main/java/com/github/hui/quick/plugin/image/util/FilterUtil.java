package com.github.hui.quick.plugin.image.util;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author YiHui
 * @date 2025/6/5
 */
public class FilterUtil {

    // 中值滤波函数
    public static BufferedImage medianFilter(BufferedImage image, int kernelSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage filteredImage = new BufferedImage(width, height, image.getType());

        int halfKernel = kernelSize / 2;

        java.util.ArrayList<Integer> values = new java.util.ArrayList<>(width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                values.clear();
                for (int ky = -halfKernel; ky <= halfKernel; ky++) {
                    for (int kx = -halfKernel; kx <= halfKernel; kx++) {
                        int nx = x + kx;
                        int ny = y + ky;

                        // 检查邻域像素是否在图像范围内
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            Color color = new Color(image.getRGB(nx, ny));
                            int gray = (int) (0.3 * color.getRed() + 0.59 * color.getGreen() + 0.11 * color.getBlue());
                            values.add(gray);
                        }
                    }
                }

                // 将 ArrayList 转换为数组
                int[] valueArray = new int[values.size()];
                for (int i = 0; i < values.size(); i++) {
                    valueArray[i] = values.get(i);
                }

                // 对邻域内的像素值进行排序
                java.util.Arrays.sort(valueArray);

                // 取中值作为当前像素的新值
                int median = valueArray[valueArray.length / 2];
                Color newColor = new Color(median, median, median);
                filteredImage.setRGB(x, y, newColor.getRGB());
            }
        }

        return filteredImage;
    }


    /**
     * 双边滤波函数
     * <p>
     * 1. 滤波半径 (radius)
     * 作用：定义了算法考虑的邻域大小。半径越大，参与计算的像素越多，平滑效果越明显，但计算量也会显著增加。
     * 选择建议：
     * 小半径 (1-3)：保留细节，适合去除细粒度噪点。
     * 中等半径 (3-5)：平衡细节保留与平滑效果，适用于大多数场景。
     * 大半径 (5+)：强平滑效果，可能模糊边缘，仅在需要大幅降噪时使用。
     * 注意：半径增大时，计算时间会呈平方级增长。
     * <p>
     * 2. 颜色空间标准差 (sigmaColor)
     * 作用：控制颜色差异对权重的影响。值越大，颜色差异的影响越小，算法会平滑更大范围的颜色，可能导致边缘模糊；值越小，颜色差异的影响越大，边缘会被更好地保留。
     * 选择建议：
     * 小值 (5-20)：保留明显的边缘和细节，适合处理有清晰边界的图像。
     * 中等值 (20-50)：平衡降噪与边缘保留，适用于大多数图像。
     * 大值 (50+)：产生类似高斯滤波的效果，边缘被平滑，适合处理噪点严重或纹理复杂的图像。
     * <p>
     * 3. 坐标空间标准差 (sigmaSpace)
     * 作用：控制空间距离对权重的影响。值越大，远处的像素也会对当前像素产生影响，导致更大范围的平滑；值越小，只有相邻的像素会被考虑。
     * 选择建议：
     * 小值 (5-10)：局部平滑，保留细节，适合处理小尺寸噪点。
     * 中等值 (10-20)：平衡局部与全局平滑，适用于大多数场景。
     * 大值 (20+)：大范围平滑，可能导致细节丢失，适合处理大面积噪点。
     * <p>
     * 4. 参数组合策略
     * 4.1保留边缘与细节：
     * - 小半径 (1-3) + 小 sigmaColor (5-20) + 小 sigmaSpace (5-10)
     * - 适合线稿提取、卡通风格图像等需要保留清晰线条的场景。
     * 4.2 降噪同时保留边缘：
     * - 中等半径 (3-5) + 中等 sigmaColor (20-50) + 中等 sigmaSpace (10-20)
     * - 适合照片去噪、人像处理等需要平衡降噪与细节的场景。
     * 4.3 强平滑效果：
     * - 大半径 (5+) + 大 sigmaColor (50+) + 大 sigmaSpace (20+)
     * - 适合模糊背景、创建艺术效果等场景。
     * <p>
     * 5. 实际调试技巧
     * 5.1 从默认值开始：通常可先尝试 radius=3、sigmaColor=10、sigmaSpace=10，再根据效果调整。
     * 5.2 渐进调整：每次只改变一个参数，观察效果变化，避免多个参数相互干扰。
     * 5.3 针对图像特性调整：
     * - 对于高对比度图像（如线稿），减小 sigmaColor 以保留边缘。
     * - 对于低对比度或纹理丰富的图像，增大 sigmaColor 以平滑颜色过渡。
     * -计算性能权衡：大半径和小标准差会显著增加计算时间，需根据应用场景平衡效果与效率。
     *
     * @param inputImage 输入的图像
     * @param radius     滤波半径
     * @param sigmaColor 颜色空间标准差
     * @param sigmaSpace 坐标空间标准差
     * @return 滤波后的图像
     */
    public static BufferedImage bilateralFilter(BufferedImage inputImage, int radius, double sigmaColor, double sigmaSpace) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 提前计算空间权重
        double[][] spaceWeights = new double[2 * radius + 1][2 * radius + 1];
        for (int ky = -radius; ky <= radius; ky++) {
            for (int kx = -radius; kx <= radius; kx++) {
                double spaceDiff = Math.sqrt(kx * kx + ky * ky);
                spaceWeights[ky + radius][kx + radius] = Math.exp(-(spaceDiff * spaceDiff) / (2 * sigmaSpace * sigmaSpace));
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int centerColor = inputImage.getRGB(x, y);
                int centerRed = (centerColor >> 16) & 0xff;
                int centerGreen = (centerColor >> 8) & 0xff;
                int centerBlue = centerColor & 0xff;

                double sumRed = 0;
                double sumGreen = 0;
                double sumBlue = 0;
                double weightSum = 0;

                for (int ky = -radius; ky <= radius; ky++) {
                    for (int kx = -radius; kx <= radius; kx++) {
                        int nx = x + kx;
                        int ny = y + ky;

                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            int neighborColor = inputImage.getRGB(nx, ny);
                            int neighborRed = (neighborColor >> 16) & 0xff;
                            int neighborGreen = (neighborColor >> 8) & 0xff;
                            int neighborBlue = neighborColor & 0xff;

                            double colorDiffRed = Math.abs(centerRed - neighborRed);
                            double colorDiffGreen = Math.abs(centerGreen - neighborGreen);
                            double colorDiffBlue = Math.abs(centerBlue - neighborBlue);

                            double colorWeightRed = Math.exp(-(colorDiffRed * colorDiffRed) / (2 * sigmaColor * sigmaColor));
                            double colorWeightGreen = Math.exp(-(colorDiffGreen * colorDiffGreen) / (2 * sigmaColor * sigmaColor));
                            double colorWeightBlue = Math.exp(-(colorDiffBlue * colorDiffBlue) / (2 * sigmaColor * sigmaColor));

                            double spaceWeight = spaceWeights[ky + radius][kx + radius];

                            double weightRed = colorWeightRed * spaceWeight;
                            double weightGreen = colorWeightGreen * spaceWeight;
                            double weightBlue = colorWeightBlue * spaceWeight;

                            sumRed += neighborRed * weightRed;
                            sumGreen += neighborGreen * weightGreen;
                            sumBlue += neighborBlue * weightBlue;
                            weightSum += weightRed;
                        }
                    }
                }

                int filteredRed = (int) (sumRed / weightSum);
                int filteredGreen = (int) (sumGreen / weightSum);
                int filteredBlue = (int) (sumBlue / weightSum);

                Color filteredColor = new Color(filteredRed, filteredGreen, filteredBlue);
                outputImage.setRGB(x, y, filteredColor.getRGB());
            }
        }

        return outputImage;
    }
}
