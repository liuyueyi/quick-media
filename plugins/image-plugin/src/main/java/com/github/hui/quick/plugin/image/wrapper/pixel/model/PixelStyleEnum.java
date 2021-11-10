package com.github.hui.quick.plugin.image.wrapper.pixel.model;

import com.github.hui.quick.plugin.image.helper.ImgPixelHelper;

import java.awt.*;

public enum PixelStyleEnum implements IPixelType {
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
        public void draw(Graphics2D g2d, Color color, int x, int y, int width, int height) {
            char ch = ImgPixelHelper.toChar(color);
            g2d.setColor(color);
            Font old = g2d.getFont();
            if (old.getSize() != width) {
                Font newFont = new Font("宋体", Font.PLAIN, width);
                g2d.setFont(newFont);
            }

            g2d.drawString(String.valueOf(ch), x, y);
        }
    },
    /**
     * 图片转字符
     */
    CHAR_GRAY {
        @Override
        public Color calculateColor(int red, int green, int blue, int size) {
            return GRAY_ALG.calculateColor(red, green, blue, size);
        }

        @Override
        public void draw(Graphics2D g2d, Color color, int x, int y, int width, int height) {
            CHAR_COLOR.draw(g2d, color, x, y, width, height);
        }
    }

    ;
}