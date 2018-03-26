package com.github.hui.quick.plugin.image.wrapper.merge.cell;

import lombok.Builder;
import lombok.Data;

import java.awt.*;

/**
 * Created by yihui on 2017/10/13.
 */
@Data
@Builder
public class LineCell implements IMergeCell {

    /**
     * 起点坐标
     */
    private int x1, y1;

    /**
     * 终点坐标
     */
    private int x2, y2;

    /**
     * 颜色
     */
    private Color color;


    /**
     * 是否是虚线
     */
    private boolean dashed;

    /**
     * 虚线样式
     */
    private Stroke stroke = CellConstants.LINE_DEFAULT_STROKE;


    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        if (!dashed) {
            g2d.drawLine(x1, y1, x2, y2);
        } else {
            Stroke origin = g2d.getStroke();
            g2d.setStroke(stroke == null ? CellConstants.LINE_DEFAULT_STROKE : stroke);
            g2d.drawLine(x1, y1, x2, y2);
            g2d.setStroke(origin);
        }
    }
}
