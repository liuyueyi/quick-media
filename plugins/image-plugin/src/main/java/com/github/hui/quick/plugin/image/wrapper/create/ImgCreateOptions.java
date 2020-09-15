package com.github.hui.quick.plugin.image.wrapper.create;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by yihui on 2017/8/16.
 */
public class ImgCreateOptions {

    public static final Font DEFAULT_FONT = new Font("宋体", Font.PLAIN, 18);

    /**
     * 绘制的背景图
     */
    private BufferedImage bgImg;


    /**
     * 生成图片的宽
     */
    private Integer imgW;


    /**
     * 生成图片的高
     */
    private Integer imgH;


    private Font font = DEFAULT_FONT;


    /**
     * 字体色
     */
    private Color fontColor = Color.BLACK;


    /**
     * 左边距
     */
    private int leftPadding;

    /**
     * 右边距
     */
    private int rightPadding;

    /**
     * 上边距
     */
    private int topPadding;

    /**
     * 底边距
     */
    private int bottomPadding;

    /**
     * 行距
     */
    private int linePadding;


    /**
     * 对齐方式
     *
     * 水平绘制时: 左对齐，居中， 右对齐
     * 垂直绘制时: 上对齐，居中，下对齐
     */
    private AlignStyle alignStyle;


    /**
     * 文本绘制方式， 水平or垂直
     */
    private DrawStyle drawStyle;

    public BufferedImage getBgImg() {
        return bgImg;
    }

    public void setBgImg(BufferedImage bgImg) {
        this.bgImg = bgImg;
    }

    public Integer getImgW() {
        return imgW;
    }

    public void setImgW(Integer imgW) {
        this.imgW = imgW;
    }

    public Integer getImgH() {
        return imgH;
    }

    public void setImgH(Integer imgH) {
        this.imgH = imgH;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
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

    public AlignStyle getAlignStyle() {
        return alignStyle;
    }

    public void setAlignStyle(AlignStyle alignStyle) {
        this.alignStyle = alignStyle;
    }

    public DrawStyle getDrawStyle() {
        return drawStyle;
    }

    public void setDrawStyle(DrawStyle drawStyle) {
        this.drawStyle = drawStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ImgCreateOptions that = (ImgCreateOptions) o;
        return leftPadding == that.leftPadding && rightPadding == that.rightPadding && topPadding == that.topPadding &&
                bottomPadding == that.bottomPadding && linePadding == that.linePadding &&
                Objects.equals(bgImg, that.bgImg) && Objects.equals(imgW, that.imgW) &&
                Objects.equals(imgH, that.imgH) && Objects.equals(font, that.font) &&
                Objects.equals(fontColor, that.fontColor) && alignStyle == that.alignStyle &&
                drawStyle == that.drawStyle;
    }

    @Override
    public int hashCode() {

        return Objects.hash(bgImg, imgW, imgH, font, fontColor, leftPadding, rightPadding, topPadding, bottomPadding,
                linePadding, alignStyle, drawStyle);
    }

    @Override
    public String toString() {
        return "ImgCreateOptions{" + "bgImg=" + bgImg + ", imgW=" + imgW + ", imgH=" + imgH + ", font=" + font +
                ", fontColor=" + fontColor + ", leftPadding=" + leftPadding + ", rightPadding=" + rightPadding +
                ", topPadding=" + topPadding + ", bottomPadding=" + bottomPadding + ", linePadding=" + linePadding +
                ", alignStyle=" + alignStyle + ", drawStyle=" + drawStyle + '}';
    }

    /**
     * 对齐方式
     */
    public enum AlignStyle {
        LEFT, CENTER, RIGHT, TOP, BOTTOM;


        private static Map<String, AlignStyle> map = new HashMap<>();

        static {
            for (AlignStyle style : AlignStyle.values()) {
                map.put(style.name(), style);
            }
        }


        public static AlignStyle getStyle(String name) {
            name = name.toUpperCase();
            if (map.containsKey(name)) {
                return map.get(name);
            }

            return LEFT;
        }
    }


    /**
     * 文本绘制方式， 水平绘制，or 垂直绘制
     */
    public enum DrawStyle {
        // 垂直绘制，从左到右
        VERTICAL_LEFT, // 垂直绘制，从右到左
        VERTICAL_RIGHT, // 水平绘制
        HORIZONTAL;


        private static Map<String, DrawStyle> map = new HashMap<>();

        static {
            for (DrawStyle style : DrawStyle.values()) {
                map.put(style.name(), style);
            }
        }


        public static DrawStyle getStyle(String name) {
            name = name.toUpperCase();
            if (map.containsKey(name)) {
                return map.get(name);
            }

            return HORIZONTAL;
        }
    }
}
