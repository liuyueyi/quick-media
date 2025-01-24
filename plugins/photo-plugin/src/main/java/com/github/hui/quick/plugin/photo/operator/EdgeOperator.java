package com.github.hui.quick.plugin.photo.operator;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.photo.PhotoOperateWrapper;
import com.github.hui.quick.plugin.photo.options.OperateOptions;
import com.github.hui.quick.plugin.photo.util.CannyFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * 提取图片线条
 *
 * @author YiHui
 * @date 2025/1/23
 */
public class EdgeOperator implements PhotoOperator {
    private final EdgeOperateOptions<?> options;

    public EdgeOperator(EdgeOperateOptions<?> options) {
        this.options = options;
    }

    @Override
    public BufferedImage operate() {
        return options.getType().operator(options.getImg(), options);
    }

    /**
     * 算法实现，来自:
     * https://blog.csdn.net/yeweij226/article/details/99747631
     */
    public enum EdgeType {
        SOBEL(new int[][]{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}}, new int[][]{{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}}, 3, 200),
        PREWITT(new int[][]{{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}}, new int[][]{{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}}, 3, 200),
        KIRSCH(new int[][]{}, new int[][]{}, 3, 200) {
            @Override
            public BufferedImage operator(BufferedImage img, EdgeOperateOptions options) {
                int threshold = options.threshold == null ? defaultThreshold : options.getThreshold();
                int width = img.getWidth();
                int height = img.getHeight();
                BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

                //Krisch算子卷积核
                double[][] krischN = {{5, 5, 5}, {-3, 0, -3}, {-3, -3, -3}};
                double[][] krischNE = {{-3, 5, 5}, {-3, 0, 5}, {-3, -3, -3}};
                double[][] krischE = {{-3, -3, 5}, {-3, 0, 5}, {-3, -3, 5}};
                double[][] krischSE = {{-3, -3, -3}, {-3, 0, 5}, {-3, 5, 5}};
                double[][] krischS = {{-3, -3, -3}, {-3, 0, -3}, {5, 5, 5}};
                double[][] krischSW = {{-3, -3, -3}, {5, 0, -3}, {5, 5, -3}};
                double[][] krischW = {{5, -3, -3}, {5, 0, -3}, {5, -3, -3}};
                double[][] krischNW = {{5, 5, -3}, {5, 0, -3}, {-3, -3, -3}};


                for (int x = 0; x < width - size + 1; x++) {
                    for (int y = 0; y < height - size + 1; y++) {
                        //设置一个数组存储八个方向的值,按顺时针方向从北极开始
                        int[] temp = {0, 0, 0, 0, 0, 0, 0, 0};

                        //对size*size区域进行卷积操作
                        for (int i = 0; i < size; i++) {
                            for (int j = 0; j < size; j++) {
                                // 灰度化
                                Color color = new Color(img.getRGB(x + i, y + j));
                                int gray = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());

                                temp[0] += gray * krischN[i][j];
                                temp[1] += gray * krischNE[i][j];
                                temp[2] += gray * krischE[i][j];
                                temp[3] += gray * krischSE[i][j];
                                temp[4] += gray * krischS[i][j];
                                temp[5] += gray * krischSW[i][j];
                                temp[6] += gray * krischW[i][j];
                                temp[7] += gray * krischNW[i][j];
                            }
                        }

                        //找出八个方向的最大值（代码为数组列表求最大值）
                        int result = Arrays.stream(temp).max().getAsInt();
                        if (result > threshold) result = options.getLineColor();
                        else result = options.getBgColor();
                        Color color = ColorUtil.int2color(result);
                        edgeImage.setRGB(x, y, color.getRGB());
                    }
                }
                return edgeImage;
            }
        },
        LAPLACIAN(new int[][]{{1, 1, 1}, {1, -8, 1}, {1, 1, 1}}, new int[][]{{1, 1, 1}, {1, -8, 1}, {1, 1, 1}}, 3, 50),
        ROBERTS(new int[][]{{1, 0}, {0, -1}}, new int[][]{{0, 1}, {-1, 0}}, 2, 50),
        CANNY(new int[][]{}, new int[][]{}, 3, 0) {
            @Override
            public BufferedImage operator(BufferedImage img, EdgeOperateOptions options) {
                //create the detector
                CannyFilter detector = new CannyFilter();

                //adjust its parameters as desired
                detector.setLowThreshold(0.5f);
                detector.setHighThreshold(1.5f);

                //apply it to an image
                detector.setSourceImage(img);
                detector.process();
                BufferedImage edges = detector.getEdgesImage();
                return edges;
            }
        },
        ;

        protected int[][] mX;
        protected int[][] mY;

        protected int size;

        protected int defaultThreshold;

        EdgeType(int[][] x, int[][] y, int size, int threshold) {
            this.mX = x;
            this.mY = y;
            this.size = size;
            this.defaultThreshold = threshold;
        }

        public BufferedImage operator(BufferedImage img, EdgeOperateOptions options) {
            int threshold = options.threshold == null ? defaultThreshold : options.getThreshold();
            int width = img.getWidth();
            int height = img.getHeight();
            BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            Graphics g2d = GraphicUtil.getG2d(edgeImage);
            for (int x = 0; x < width - size + 1; x++) {
                for (int y = 0; y < height - size + 1; y++) {
                    //设置x,y方向的结果变量，一定要在循环内初始化，因为每次循环都要清零重新加
                    int tempX = 0;
                    int tempY = 0;
                    //对size*size区域进行卷积操作
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            // 灰度化
                            Color color = new Color(img.getRGB(x + i, y + j));
                            int gray = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
                            tempX += gray * mX[i][j];
                            tempY += gray * mY[i][j];
                        }
                    }
                    //求梯度值
                    int result = (int) Math.sqrt(tempX * tempX + tempY * tempY);
                    if (result > threshold) result = options.getLineColor();
                    else result = options.getBgColor();
                    Color color = ColorUtil.int2color(result);
                    if (result == options.getLineColor()) {
                        // 绘制线条
                        g2d.setColor(color);
                        g2d.drawLine(x + size / 2, y + size / 2, x + size / 2, y + size / 2);
                    } else {
                        // 绘制背景色
                        edgeImage.setRGB(x, y, color.getRGB());
                    }
                }
            }
            g2d.dispose();
            return edgeImage;
        }
    }

    public static class EdgeOperateOptions<T> extends OperateOptions<T> {
        private EdgeType type = EdgeType.SOBEL;

        private Integer threshold;

        /**
         * 背景色，默认为黑色
         */
        private Integer bgColor = Color.BLACK.getRGB();

        /**
         * 线条色，默认为白色
         */
        private Integer lineColor = Color.WHITE.getRGB();

        public EdgeOperateOptions(T delegate) {
            super(delegate);
        }

        public EdgeType getType() {
            return type;
        }

        public EdgeOperateOptions<T> setType(EdgeType type) {
            this.type = type;
            return this;
        }

        public Integer getThreshold() {
            return threshold;
        }

        public EdgeOperateOptions<T> setThreshold(Integer threshold) {
            this.threshold = threshold;
            return this;
        }

        public Integer getBgColor() {
            return bgColor;
        }

        public EdgeOperateOptions<T> setBgColor(Integer bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        public EdgeOperateOptions<T> setBgColor(Color bgColor) {
            this.bgColor = bgColor.getRGB();
            return this;
        }

        public Integer getLineColor() {
            return lineColor;
        }

        public EdgeOperateOptions<T> setLineColor(Integer lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public EdgeOperateOptions<T> setLineColor(Color lineColor) {
            this.lineColor = lineColor.getRGB();
            return this;
        }

        @Override
        public PhotoOperator operator() {
            return new EdgeOperator(this);
        }

        public static EdgeOperateOptions<PhotoOperateWrapper> type() {
            return new EdgeOperateOptions<>(PhotoOperateWrapper.DEFAULT_INSTANCE);
        }
    }
}
