package com.github.hui.quick.plugin.image.wrapper.merge.cell;

import java.awt.*;
import java.util.Objects;

/**
 * Created by yihui on 2017/10/13.
 */
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
     * 虚线样式，指定线宽等，如 {@link CellConstants#RECT_DEFAULT_DASH}
     */
    private Stroke stroke;


    /**
     * 圆角弧度
     */
    private int radius;

    public RectCell() {
    }

    public RectCell(int x, int y, int w, int h, Color color, Stroke stroke, int radius) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = color;
        this.stroke = stroke;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        if (stroke == null) {
            g2d.drawRoundRect(x, y, w, h, radius, radius);
        } else {
            Stroke stroke = g2d.getStroke();
            g2d.setStroke(this.stroke);
            g2d.drawRoundRect(x, y, w, h, radius, radius);
            g2d.setStroke(stroke);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RectCell rectCell = (RectCell) o;
        return x == rectCell.x && y == rectCell.y && w == rectCell.w && h == rectCell.h && radius == rectCell.radius &&
                Objects.equals(color, rectCell.color) && Objects.equals(stroke, rectCell.stroke);
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y, w, h, color, stroke, radius);
    }

    @Override
    public String toString() {
        return "RectCell{" + "x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", color=" + color + ", stroke=" +
                stroke + ", radius=" + radius + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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
         * 虚线样式
         */
        private Stroke stroke;


        /**
         * 圆角弧度
         */
        private int radius;

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder w(int w) {
            this.w = w;
            return this;
        }

        public Builder h(int h) {
            this.h = h;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder stroke(Stroke stroke) {
            this.stroke = stroke;
            return this;
        }

        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        public RectCell build() {
            return new RectCell(x, y, w, h, color, stroke, radius);
        }
    }
}
