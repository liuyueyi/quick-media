package com.github.hui.quick.plugin.image.wrapper.merge.cell;

import lombok.Builder;
import lombok.Data;

import java.awt.*;

/**
 * 填充矩形框
 * Created by yihui on 2017/10/16.
 */
@Data
@Builder
public class RectFillCell implements IMergeCell {

    private Font font;

    private Color color;


    private int x,y,w,h;

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(color);;
        g2d.fillRect(x, y, w, h);
    }
}
