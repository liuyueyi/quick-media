package com.github.hui.quick.plugin.image.wrapper.merge.cell;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Created by yihui on 2017/10/13.
 */
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

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
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
        ImgCell imgCell = (ImgCell) o;
        return Objects.equals(img, imgCell.img) && Objects.equals(x, imgCell.x) && Objects.equals(y, imgCell.y) &&
                Objects.equals(w, imgCell.w) && Objects.equals(h, imgCell.h);
    }

    @Override
    public int hashCode() {

        return Objects.hash(img, x, y, w, h);
    }

    @Override
    public String toString() {
        return "ImgCell{" + "img=" + img + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BufferedImage img;
        private int x, y, w, h;

        public Builder img(BufferedImage img) {
            this.img = img;
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

        public ImgCell build() {
            ImgCell imgCell = new ImgCell();
            imgCell.img = img;
            imgCell.x = x;
            imgCell.y = y;
            // 默认ImgCell的宽高等图片宽高
            imgCell.w = w == 0 ? img.getWidth() : w;
            imgCell.h = h == 0 ? img.getHeight() : h;
            return imgCell;
        }
    }
}
