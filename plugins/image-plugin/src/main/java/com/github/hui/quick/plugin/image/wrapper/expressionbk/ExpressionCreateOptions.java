package com.github.hui.quick.plugin.image.wrapper.expressionbk;


import java.awt.*;
import java.util.List;
import java.util.Objects;


/**
 * Created by yihui on 2017/9/14.
 */
public class ExpressionCreateOptions {
    /**
     * 四周的边框
     */
    private Integer padding = 40;

    /**
     * 生成表情图的宽
     */
    private Integer w;


    /**
     * 生成表情图的高
     */
    private Integer h;


    /**
     * 字体
     */
    private Font font;


    private Integer logoW;

    private Integer logoH;

    private int logoX;

    private int logoY;

    /**
     * 背景色
     */
    private Color bgColor = Color.WHITE;


    /**
     * 每一帧的内容
     */
    private List<ExpressionFrameEntity> frameList;


    /**
     * 延迟时间
     */
    private int delay = 100;

    public Integer getPadding() {
        return padding;
    }

    public void setPadding(Integer padding) {
        this.padding = padding;
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

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Integer getLogoW() {
        return logoW;
    }

    public void setLogoW(Integer logoW) {
        this.logoW = logoW;
    }

    public Integer getLogoH() {
        return logoH;
    }

    public void setLogoH(Integer logoH) {
        this.logoH = logoH;
    }

    public int getLogoX() {
        return logoX;
    }

    public void setLogoX(int logoX) {
        this.logoX = logoX;
    }

    public int getLogoY() {
        return logoY;
    }

    public void setLogoY(int logoY) {
        this.logoY = logoY;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public List<ExpressionFrameEntity> getFrameList() {
        return frameList;
    }

    public void setFrameList(List<ExpressionFrameEntity> frameList) {
        this.frameList = frameList;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExpressionCreateOptions that = (ExpressionCreateOptions) o;
        return logoX == that.logoX && logoY == that.logoY && delay == that.delay &&
                Objects.equals(padding, that.padding) && Objects.equals(w, that.w) && Objects.equals(h, that.h) &&
                Objects.equals(font, that.font) && Objects.equals(logoW, that.logoW) &&
                Objects.equals(logoH, that.logoH) && Objects.equals(bgColor, that.bgColor) &&
                Objects.equals(frameList, that.frameList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(padding, w, h, font, logoW, logoH, logoX, logoY, bgColor, frameList, delay);
    }

    @Override
    public String toString() {
        return "ExpressionCreateOptions{" + "padding=" + padding + ", w=" + w + ", h=" + h + ", font=" + font +
                ", logoW=" + logoW + ", logoH=" + logoH + ", logoX=" + logoX + ", logoY=" + logoY + ", bgColor=" +
                bgColor + ", frameList=" + frameList + ", delay=" + delay + '}';
    }
}
