package com.github.hui.quick.plugin.image.wrapper.merge.cell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

/**
 * Created by yihui on 2017/10/13.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RectCell implements IMergeCell {

    /**
     * 起始坐标
     */
    private int x, y;

    /**
     * 矩形宽高
     */
    private int w, h;


    /**
     * 颜色
     */
    private Color color;


    /**
     * 是否为虚线
     */
    private boolean dashed;


    /**
     * 虚线样式
     */
    private Stroke stroke;


    /**
     * 圆角弧度
     */
    private int radius;


    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        if (!dashed) {
            g2d.drawRoundRect(x, y, w, h, radius, radius);
        } else {
            Stroke stroke = g2d.getStroke();
            g2d.setStroke(stroke);
            g2d.drawRoundRect(x, y, w, h, radius, radius);
            g2d.setStroke(stroke);
        }
    }
}
