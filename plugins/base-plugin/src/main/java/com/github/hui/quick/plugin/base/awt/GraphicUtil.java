package com.github.hui.quick.plugin.base.awt;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Created by yihui on 2017/8/16.
 */
public class GraphicUtil {

    public static BufferedImage pngToJpg(BufferedImage img, Color bg) {
        BufferedImage newBufferedImage = new BufferedImage(img.getWidth(),
                img.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(img, 0, 0, bg, null);
        return newBufferedImage;
    }

    public static BufferedImage scaleImg(int w, int h, BufferedImage img) {
        BufferedImage bf = new BufferedImage(w, h, img.getType());
        Graphics2D g2d = getG2d(bf);
        g2d.drawImage(img, 0, 0, w, h, null);
        return bf;
    }

    public static BufferedImage createImg(int w, int h, int type) {
        return createImg(w, h, 0, 0, type, null, null);
    }

    public static BufferedImage createImg(int w, int h, BufferedImage img) {
        return createImg(w, h, 0, 0, img);
    }


    public static BufferedImage createImg(int w, int h, int offsetX, int offsetY, BufferedImage img) {
        return createImg(w, h, offsetX, offsetY, img, null);
    }

    public static BufferedImage createImg(int w, int h, int offsetX, int offsetY, BufferedImage img, Color fillColor) {
        return createImg(w, h, offsetX, offsetY, BufferedImage.TYPE_INT_ARGB, img, fillColor);
    }

    public static BufferedImage createImg(int w, int h, int offsetX, int offsetY, int imgType, BufferedImage img, Color fillColor) {
        BufferedImage bf = new BufferedImage(w, h, imgType);
        if (fillColor == null && img == null) {
            return bf;
        }

        Graphics2D g2d = getG2d(bf);
        if (fillColor != null) {
            g2d.setColor(fillColor);
            g2d.fillRect(0, 0, w, h);
        }

        if (img != null) {
            g2d.setComposite(AlphaComposite.Src);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(img, offsetX, offsetY, null);
        }
        g2d.dispose();
        return bf;
    }

    public static Graphics2D getG2d(BufferedImage bf) {
        Graphics2D g2d = Optional.ofNullable(bf).orElse(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)).createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        // 抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        return g2d;
    }
}
