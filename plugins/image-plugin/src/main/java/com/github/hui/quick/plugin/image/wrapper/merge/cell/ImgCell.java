package com.github.hui.quick.plugin.image.wrapper.merge.cell;

import lombok.Builder;
import lombok.Data;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yihui on 2017/10/13.
 */
@Data
@Builder
public class ImgCell implements IMergeCell {

    private BufferedImage img;

    private Integer x, y, w, h;

    @Override
    public void draw(Graphics2D g2d) {
        if (w == null) {
            w = img.getWidth();
        }


        if (h == null) {
            h = img.getHeight();
        }

        g2d.drawImage(img, x, y, w, h, null);
    }
}
