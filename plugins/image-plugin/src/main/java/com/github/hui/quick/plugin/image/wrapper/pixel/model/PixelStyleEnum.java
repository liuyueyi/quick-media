package com.github.hui.quick.plugin.image.wrapper.pixel.model;

import com.github.hui.quick.plugin.image.helper.ImgPixelHelper;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelOptions;
import com.github.hui.quick.plugin.image.wrapper.pixel.context.PixelContextHolder;

import java.awt.*;
import java.lang.String;

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
            char ch = ImgPixelHelper.toChar(options.getChars(), g2d.getColor());
            if (g2d.getFont() == null || g2d.getFont().getSize() != options.getBlockSize()) {
                g2d.setFont(options.getFont());
            }
            PixelContextHolder.addChar(y, ch);
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
            if (g2d.getFont() == null || g2d.getFont().getSize() != options.getBlockSize()) {
                g2d.setFont(options.getFont());
            }
            PixelContextHolder.addChar(y, ch);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(ch), x, y);
        }
    },
    ;
}