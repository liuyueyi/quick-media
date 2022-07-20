package com.github.hui.quick.plugin.image.wrapper.wartermark.remove.operator;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.util.PixelUtil;
import com.github.hui.quick.plugin.image.wrapper.wartermark.remove.WaterMarkRemoveOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author YiHui
 * @date 2022/7/13
 */
public enum WaterMarkRemoveTypeEnum implements WaterMarkRemoveOperator {
    /**
     * 像素方式，默认方式
     */
    PIXEL("pixel") {
        @Override
        public BufferedImage remove(WaterMarkRemoveOptions options) {
            BufferedImage source = options.getImg();
            BufferedImage out = GraphicUtil.createImg(source.getWidth(), source.getHeight(), source.getType());

            Set<String> proceed = new HashSet<>(source.getWidth() * source.getHeight());
            Graphics2D g2d = GraphicUtil.getG2d(out);
            for (int i = 0; i < source.getWidth(); i++) {
                for (int j = 0; j < source.getHeight(); j++) {
                    Color color;
                    if (inWater(i, j, options)) {
                        if (proceed.contains(i + "_" + j)) {
                            continue;
                        }

                        color = PixelUtil.getAverage(PixelUtil.getPixels(source, i, j, options.getPixelSize(), options.getPixelSize()));
                        for (int pi = 0; pi < options.getPixelSize(); pi++) {
                            for (int pj = 0; pj < options.getPixelSize(); pj++) {
                                // 标记已经处理过的位置，避免重复渲染
                                proceed.add(String.valueOf(i + pi) + "_" + String.valueOf(j + pj));
                            }
                        }
                        g2d.setColor(color);
                        g2d.fillRect(i, j, options.getPixelSize(), options.getPixelSize());
                    } else {
                        color = new Color(source.getRGB(i, j));
                        g2d.setColor(color);
                        g2d.fillRect(i, j, 1, 1);
                    }
                }
            }
            // 快速回收
            proceed = null;
            g2d.dispose();
            return out;
        }

        @Override
        public boolean match(String type) {
            return this.getType().equalsIgnoreCase(type);
        }
    },
    /**
     * 指定颜色填充
     */
    FILL("fill") {
        @Override
        public BufferedImage remove(WaterMarkRemoveOptions options) {
            BufferedImage source = options.getImg();
            BufferedImage out = GraphicUtil.createImg(source.getWidth(), source.getHeight(), source.getType());

            Graphics2D g2d = GraphicUtil.getG2d(out);
            boolean first = true;
            for (int i = 0; i < source.getWidth(); i++) {
                for (int j = 0; j < source.getHeight(); j++) {
                    if (inWater(i, j, options)) {
                        if (!first) {
                            continue;
                        }
                        g2d.setColor(options.getFillColor());
                        g2d.fillRect(i, j, options.getWaterMarkW(), options.getWaterMarkH());
                        first = false;
                    } else {
                        g2d.setColor(new Color(source.getRGB(i, j)));
                        g2d.fillRect(i, j, 1, 1);
                    }
                }
            }
            g2d.dispose();
            return out;
        }
    },
    /**
     * 背景色自动填充
     */
    BG("bg") {
        @Override
        public BufferedImage remove(WaterMarkRemoveOptions options) {
            BufferedImage source = options.getImg();
            BufferedImage out = GraphicUtil.createImg(source.getWidth(), source.getHeight(), source.getType());

            Graphics2D g2d = GraphicUtil.getG2d(out);
            for (int i = 0; i < source.getWidth(); i++) {
                for (int j = 0; j < source.getHeight(); j++) {
                    Color color;
                    if (inWater(i, j, options)) {
                        color = calculateBgAvgColor(source, i, j, options);
                    } else {
                        color = (new Color(source.getRGB(i, j)));
                    }
                    g2d.setColor(color);
                    g2d.fillRect(i, j, 1, 1);
                }
            }
            g2d.dispose();
            return out;
        }

        public Color calculateBgAvgColor(BufferedImage img, int x, int currentY, WaterMarkRemoveOptions options) {
            List<Integer> list = new ArrayList<>();
            int y = options.getWaterMarkY();
            int h = options.getWaterMarkH();
            int downY = y + Float.valueOf(h * options.getUpDownRate()).intValue();
            if (currentY >= downY) {
                int downSize = Float.valueOf((h + y - downY) * options.getDownRange()).intValue();
                // 从下部分获取颜色均值
                for (int t = 0; t < downSize; t++) {
                    if (y + h + t < img.getHeight()) {
                        list.add(img.getRGB(x, y + h + t));
                    }
                }
            } else {
                // 从上部分获取颜色均值
                int upSize = Float.valueOf((downY - y) * options.getUpRange()).intValue();
                for (int t = 0; t < upSize; t++) {
                    if (y - t >= 0) {
                        list.add(img.getRGB(x, y - t));
                    }
                }
            }
            return PixelUtil.getAverage(list);
        }
    },
    ;

    private String type;

    WaterMarkRemoveTypeEnum(String type) {
        this.type = type;
    }

    public static WaterMarkRemoveTypeEnum typeOf(String type) {
        for (WaterMarkRemoveTypeEnum e : values()) {
            if (e.type.equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean match(String type) {
        return this.getType().equalsIgnoreCase(type);
    }
}
