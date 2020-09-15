package com.github.hui.quick.plugin.image.wrapper.emoticon;

import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by yihui on 2017/9/19.
 */
public class EmotionOptions {

    private int w;

    private int h;

    /**
     * 文字内容
     */
    private String[] content;

    /**
     * 文字对应占用的高度or宽度
     */
    private int contentSize;

    private Font font;



    private boolean imgFirst;

    private Color fontColor;


    /**
     * gif 中对应的每一帧图片
     */
    private List<BufferedImage> imgs;

    /**
     * gif 图实际绘制的宽
     */
    private int gifW;

    /**
     * gif 图实际绘制的高
     */
    private int gifH;

    /**
     * gif 图时间
     */
    private int delay;



    private ImgCreateOptions.DrawStyle drawStyle;


    private int leftPadding;

    private int rightPadding;

    private int topPadding;

    private int bottomPadding;

    private int linePadding;

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

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public int getContentSize() {
        return contentSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public boolean isImgFirst() {
        return imgFirst;
    }

    public void setImgFirst(boolean imgFirst) {
        this.imgFirst = imgFirst;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public List<BufferedImage> getImgs() {
        return imgs;
    }

    public void setImgs(List<BufferedImage> imgs) {
        this.imgs = imgs;
    }

    public int getGifW() {
        return gifW;
    }

    public void setGifW(int gifW) {
        this.gifW = gifW;
    }

    public int getGifH() {
        return gifH;
    }

    public void setGifH(int gifH) {
        this.gifH = gifH;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public ImgCreateOptions.DrawStyle getDrawStyle() {
        return drawStyle;
    }

    public void setDrawStyle(ImgCreateOptions.DrawStyle drawStyle) {
        this.drawStyle = drawStyle;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public int getLinePadding() {
        return linePadding;
    }

    public void setLinePadding(int linePadding) {
        this.linePadding = linePadding;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmotionOptions that = (EmotionOptions) o;
        return w == that.w && h == that.h && contentSize == that.contentSize && imgFirst == that.imgFirst &&
                gifW == that.gifW && gifH == that.gifH && delay == that.delay && leftPadding == that.leftPadding &&
                rightPadding == that.rightPadding && topPadding == that.topPadding &&
                bottomPadding == that.bottomPadding && linePadding == that.linePadding &&
                Arrays.equals(content, that.content) && Objects.equals(font, that.font) &&
                Objects.equals(fontColor, that.fontColor) && Objects.equals(imgs, that.imgs) &&
                drawStyle == that.drawStyle;
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(w, h, contentSize, font, imgFirst, fontColor, imgs, gifW, gifH, delay, drawStyle,
                leftPadding, rightPadding, topPadding, bottomPadding, linePadding);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return "EmotionOptions{" + "w=" + w + ", h=" + h + ", content=" + Arrays.toString(content) + ", contentSize=" +
                contentSize + ", font=" + font + ", imgFirst=" + imgFirst + ", fontColor=" + fontColor + ", imgs=" +
                imgs + ", gifW=" + gifW + ", gifH=" + gifH + ", delay=" + delay + ", drawStyle=" + drawStyle +
                ", leftPadding=" + leftPadding + ", rightPadding=" + rightPadding + ", topPadding=" + topPadding +
                ", bottomPadding=" + bottomPadding + ", linePadding=" + linePadding + '}';
    }
}
