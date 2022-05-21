package com.github.hui.quick.plugin.image.wrapper.pixel;

import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.IPixelStyle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Predicate;

/**
 * @author yihui
 * @date 21/11/10
 */
public class ImgPixelOptions {
    /**
     * 原图
     */
    private BufferedImage source;

    /**
     * gif 图
     */
    private GifDecoder gifSource;

    /**
     * 转换类型
     */
    private IPixelStyle pixelType;

    /**
     * 对于转字符图时，它控制字符大小
     * 对于灰度/像素处理时，这个表示像素化的处理操作
     */
    private int blockSize;

    /**
     * 字体大小
     */
    private int fontSize;

    /**
     * 字符图时，用于渲染的字符集
     */
    private String chars;

    /**
     * 字符字体
     */
    private Font font;

    /**
     * 缩放比例，1 表示输出的图不缩放； > 1，表示生成的图，按倍数扩大
     */
    private Double rate;

    /**
     * 输出图片类型
     */
    private String picType;

    /**
     * 字符图/svg图的背景色
     */
    private Color bgColor;

    /**
     * 字符图/svg图的字体色
     */
    private Color fontColor;

    /**
     * 背景色判断方式，传参为intColor, 如果返回true，表示认定为背景色；否则不是
     */
    private Predicate<Integer> bgPredicate;

    public BufferedImage getSource() {
        return source;
    }

    public void setSource(BufferedImage source) {
        this.source = source;
    }

    public GifDecoder getGifSource() {
        return gifSource;
    }

    public void setGifSource(GifDecoder gifSource) {
        this.gifSource = gifSource;
    }

    public IPixelStyle getPixelType() {
        return pixelType;
    }

    public void setPixelType(IPixelStyle pixelType) {
        this.pixelType = pixelType;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getRate() {
        return rate;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setBgPredicate(Predicate<Integer> predicate) {
        this.bgPredicate = predicate;
    }

    public Predicate<Integer> getBgPredicate() {
        return bgPredicate;
    }
}
