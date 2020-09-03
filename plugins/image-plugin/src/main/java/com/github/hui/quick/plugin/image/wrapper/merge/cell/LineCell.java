package com.github.hui.quick.plugin.image.wrapper.merge.cell;


import java.awt.*;
import java.util.Objects;

/**
 * Created by yihui on 2017/10/13.
 */
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

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isDashed() {
        return dashed;
    }

    public void setDashed(boolean dashed) {
        this.dashed = dashed;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineCell lineCell = (LineCell) o;
        return x1 == lineCell.x1 && y1 == lineCell.y1 && x2 == lineCell.x2 && y2 == lineCell.y2 &&
                dashed == lineCell.dashed && Objects.equals(color, lineCell.color) &&
                Objects.equals(stroke, lineCell.stroke);
    }

    @Override
    public int hashCode() {

        return Objects.hash(x1, y1, x2, y2, color, dashed, stroke);
    }

    @Override
    public String toString() {
        return "LineCell{" + "x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", color=" + color +
                ", dashed=" + dashed + ", stroke=" + stroke + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
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

        public Builder x1(int x1) {
            this.x1 = x1;
            return this;
        }

        public Builder x2(int x2) {
            this.x2 = x2;
            return this;
        }

        public Builder y1(int y1) {
            this.y1 = y1;
            return this;
        }

        public Builder y2(int y2) {
            this.y2 = y2;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder dashed(boolean dashed) {
            this.dashed = dashed;
            return this;
        }

        public Builder stroke(Stroke stroke) {
            this.stroke = stroke;
            return this;
        }

        public LineCell build() {
            LineCell lineCell = new LineCell();
            lineCell.x1 = x1;
            lineCell.x2 = x2;
            lineCell.y1 = y1;
            lineCell.y2 = y2;
            lineCell.dashed = dashed;
            lineCell.color = color;
            lineCell.stroke = stroke;
            return lineCell;
        }
    }
}
