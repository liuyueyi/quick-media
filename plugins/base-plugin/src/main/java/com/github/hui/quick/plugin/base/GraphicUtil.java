package com.github.hui.quick.plugin.base;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * Created by yihui on 2017/8/16.
 */
public class GraphicUtil {

    public static BufferedImage createImg(int w, int h, BufferedImage img) {
        return createImg(w, h, 0, 0, img);
    }


    public static BufferedImage createImg(int w, int h, int offsetX, int offsetY, BufferedImage img) {
        BufferedImage bf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        if (img == null) {
            return bf;
        }


        Graphics2D g2d = bf.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(img, offsetX, offsetY, null);
        g2d.dispose();
        return bf;
    }


    public static Graphics2D getG2d(BufferedImage bf) {
        Graphics2D g2d = Optional.ofNullable(bf).orElse(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)).createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
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
