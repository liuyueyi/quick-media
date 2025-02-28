package com.github.hui.quick.plugin.image.wrapper.pixel.model;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.image.helper.ImgPixelHelper;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelOptions;
import com.github.hui.quick.plugin.image.wrapper.pixel.context.PixelContextHolder;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 系统提供的渲染枚举类
 *
 * @author yihui
 * @date 21/11/8
 */

public enum PixelStyleEnum implements IPixelStyle {
    /**
     * 灰度公式
     */
    GRAY_ALG {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            int avg = Math.round((red * 0.299f + green * 0.587f + blue * 0.114f) / size);
            return new Color(avg, avg, avg);
        }
    },
    /**
     * 灰度均值
     */
    GRAY_AVG {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            int avg = Math.round((red + green + blue) / 3.0f / size);
            return new Color(avg, avg, avg);
        }
    },
    /**
     * 颜色均值 -- 适用于像素块处理
     */
    PIXEL_COLOR_AVG {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            red = Math.round(red / (float) size);
            green = Math.round(green / (float) size);
            blue = Math.round(blue / (float) size);
            return new Color(red, green, blue);
        }
    },
    /**
     * 图片转字符
     */
    CHAR_COLOR {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            return PIXEL_COLOR_AVG.calculateColor(red, green, blue, size);
        }

        @Override
        public void draw(Graphics2D g2d, ImgPixelOptions options, int x, int y) {
            if (g2d.getFont() == null || g2d.getFont().getSize() != options.getBlockSize()) {
                g2d.setFont(options.getFont());
            }

            int blockSize = options.getBlockSize();
            int[] colors = ImgPixelHelper.getPixels(options.getSource(), x, y, blockSize, blockSize);
            Color avgColor = ImgPixelHelper.getAverageColor(colors);

            char ch;
            if (options.getBgPredicate().test(avgColor.getRGB())) {
                // 背景色，使用背景字符进行填充
                ch = options.getBgChar();
                PixelContextHolder.addChar(y, options.getBgChar());
                if (options.getCharSeparate() != null) PixelContextHolder.addChar(y, options.getCharSeparate());
            } else {
                if (!StringUtils.isBlank(options.getChars())) {
                    // 非背景色
                    ch = ImgPixelHelper.toChar(options.getChars(), avgColor);
                    PixelContextHolder.addChar(y, ch, avgColor);
                } else {
                    // 没有设置字符时的场景，表明使用颜色进行填充
                    String key = PixelContextHolder.putColor(avgColor, options.getSameColorThreshold());
                    for (int i = 0; i < key.length(); i++) {
                        PixelContextHolder.addChar(y, key.charAt(i));
                    }
                    PixelContextHolder.addChar(y, options.getCharSeparate() == null ? ',' : options.getCharSeparate());
                    ch = key.charAt(0);
                }
            }
            g2d.setColor(avgColor);
            g2d.drawString(String.valueOf(ch), x, y);
        }
    },
    /**
     * 图片转灰度字符
     */
    CHAR_GRAY {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            return GRAY_ALG.calculateColor(red, green, blue, size);
        }

        @Override
        public void draw(Graphics2D g2d, ImgPixelOptions options, int x, int y) {
            CHAR_COLOR.draw(g2d, options, x, y);
        }
    },

    /**
     * 图片转纯黑白字符
     */
    CHAR_BLACK {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            return GRAY_ALG.calculateColor(red, green, blue, size);
        }

        @Override
        public void draw(Graphics2D g2d, ImgPixelOptions options, int x, int y) {
            char ch = ImgPixelHelper.toChar(options.getChars(), g2d.getColor());
            if (options.getBgPredicate().test(g2d.getColor().getRGB())) {
                // 背景色，使用背景字符进行填充
                PixelContextHolder.addChar(y, options.getBgChar());
            } else {
                PixelContextHolder.addChar(y, ch);
            }

            if (g2d.getFont() == null || g2d.getFont().getSize() != options.getBlockSize()) {
                g2d.setFont(options.getFont());
            }
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(ch), x, y);
        }
    },

    /**
     * 只针对有颜色的边框进行渲染
     */
    BLACK_CHAR_BORDER {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            return GRAY_ALG.calculateColor(red, green, blue, size);
        }

        @Override
        public void render(Graphics2D g2d, BufferedImage source, ImgPixelOptions options, int x, int y) {
            if (g2d.getFont() == null || g2d.getFont().getSize() != options.getBlockSize()) {
                g2d.setFont(options.getFont());
            }

            int blockSize = options.getBlockSize();
            int[] colors = ImgPixelHelper.getPixels(source, x, y, blockSize, blockSize);
            boolean allBg = true;
            List<Integer> borderColors = new ArrayList<>();
            for (int i = 0; i < colors.length; i++) {
                if (!options.getBgPredicate().test(colors[i])) {
                    // 边框颜色分布
                    borderColors.add(colors[i]);

                    // 这个区域内全部都是背景时，才表示为背景图
                    allBg = false;
                }
            }
            if (allBg) {
                // 背景，直接跳过
                PixelContextHolder.addChar(y, options.getBgChar());
                return;
            }

            Color renderColor = ImgPixelHelper.getAverageColor(borderColors);
            char ch = ImgPixelHelper.toChar(options.getChars(), renderColor);
            PixelContextHolder.addChar(y, ch, renderColor);
            g2d.setColor(renderColor);
            g2d.drawString(String.valueOf(ch), x, y);
        }
    },

    /**
     * 根据字符顺序绘画
     */
    CHAR_SEQ_SCALE_UP {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            return null;
        }

        @Override
        public boolean scaleUp() {
            return true;
        }

        @Override
        public void draw(Graphics2D g2d, ImgPixelOptions options, int x, int y) {
            int index = PixelContextHolder.getPixelChar().getAndUpdateSeqIndex(options.getChars().length());
            char ch = options.getChars().charAt(index);
            int offsetSize = (options.getBlockSize() - options.getFontSize()) / 2;
            g2d.drawString(String.valueOf(ch), x * options.getBlockSize() + offsetSize, y * options.getBlockSize() + offsetSize);
        }

        @Override
        public void render(Graphics2D g2d, BufferedImage source, ImgPixelOptions options, int x, int y) {
            int color = source.getRGB(x, y);
            if (options.getBgPredicate().test(color)) {
                // 背景，直接跳过
                return;
            }

            if (g2d.getFont() == null
                    || !Objects.equals(g2d.getFont().getName(), options.getFont().getName())
                    || g2d.getFont().getSize() != options.getFontSize()) {
                g2d.setFont(options.getFont());
            }
            g2d.setColor(ColorUtil.int2color(color));
            draw(g2d, options, x, y);
        }
    };
}