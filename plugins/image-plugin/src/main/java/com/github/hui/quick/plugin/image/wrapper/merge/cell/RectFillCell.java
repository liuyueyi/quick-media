package com.github.hui.quick.plugin.image.wrapper.merge.cell;


import java.awt.*;
import java.util.Objects;

/**
 * 填充矩形框
 * Created by yihui on 2017/10/16.
 */
public class RectFillCell implements IMergeCell {

    private Font font;

    private Color color;


    private int x, y, w, h;

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(color);
        ;
        g2d.fillRect(x, y, w, h);
    }

    public RectFillCell() {
    }

    public RectFillCell(Font font, Color color, int x, int y, int w, int h) {
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RectFillCell that = (RectFillCell) o;
        return x == that.x && y == that.y && w == that.w && h == that.h && Objects.equals(font, that.font) &&
                Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {

        return Objects.hash(font, color, x, y, w, h);
    }

    @Override
    public String toString() {
        return "RectFillCell{" + "font=" + font + ", color=" + color + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" +
                h + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Font font;

        private Color color;


        private int x, y, w, h;

        public Builder font(Font font) {
            this.font = font;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

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

        public RectFillCell build() {
            return new RectFillCell(font, color, x, y, w, h);
        }
    }
}
