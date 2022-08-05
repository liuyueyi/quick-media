package com.github.hui.quick.plugin.qrcode.v3.constants;

import com.github.hui.quick.plugin.qrcode.v3.entity.svg.SvgTemplate;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author YiHui
 * @date 2022/8/2
 */
public class RenderFunc {

    @FunctionalInterface
    public interface GeometryDrawFunc {
        /**
         * 几何图形填充
         *
         * @param g2d
         * @param x
         * @param y
         * @param w
         * @param h
         */
        void draw(Graphics2D g2d, int x, int y, int w, int h);
    }

    @FunctionalInterface
    public interface ImgDrawFunc {
        /**
         * 图片资源填充
         *
         * @param g2d
         * @param img
         * @param x
         * @param y
         * @param w
         * @param h
         */
        void draw(Graphics2D g2d, BufferedImage img, int x, int y, int w, int h);
    }

    @FunctionalInterface
    public interface TxtImgDrawFunc {
        /**
         * 绘制文字
         *
         * @param g2d
         * @param txt
         * @param x
         * @param y
         * @param size
         */
        void draw(Graphics2D g2d, String txt, int x, int y, int size);
    }

    @FunctionalInterface
    public interface SvgDrawFunc {
        void draw(SvgTemplate svg, String svgId, int x, int y, int w, int h);
    }
}
