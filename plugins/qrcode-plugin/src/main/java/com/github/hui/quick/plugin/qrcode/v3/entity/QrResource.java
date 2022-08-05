package com.github.hui.quick.plugin.qrcode.v3.entity;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.base.gif.GifHelper;
import com.github.hui.quick.plugin.qrcode.constants.QuickQrUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.TxtMode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author YiHui
 * @date 2022/7/20
 */
public class QrResource {
    /**
     * 动态背景图
     */
    private GifDecoder gif;

    /**
     * 背景图
     */
    private BufferedImage img;

    /**
     * 图样式
     */
    private PicStyle picStyle = PicStyle.NORMAL;

    /**
     * 图片的圆角比例，默认为原图的 1/8
     */
    private float cornerRadius = 1 / 8.0f;

    /**
     * 文字
     */
    private String text;

    /**
     * 文字二维码渲染模式
     */
    private TxtMode txtMode;

    /**
     * 生成文字二维码时的字体
     */
    private String fontName;

    /**
     * 字体样式
     * <p>
     * {@link Font#PLAIN} 0
     * {@link Font#BOLD}  1
     * {@link Font#ITALIC} 2
     */
    private int fontStyle;

    /**
     * 文字顺序处理是
     */
    private AtomicInteger indexCnt;

    /**
     * svg tag
     */
    private String svg;


    public GifDecoder getGif() {
        return gif;
    }

    public QrResource setGif(GifDecoder gif) {
        this.gif = gif;
        return this;
    }

    public QrResource setGif(String gif) throws IOException {
        this.gif = GifHelper.loadGif(gif);
        return this;
    }

    public BufferedImage getImg() {
        return img;
    }

    public QrResource setImg(BufferedImage img) {
        this.img = img;
        return this;
    }

    public QrResource setImg(String img) throws IOException {
        return setImg(ImageLoadUtil.getImageByPath(img));
    }

    public String getText() {
        if (txtMode == null) {
            setTxtMode(TxtMode.FULL);
        }
        return txtMode.txt(text, indexCnt);
    }

    public QrResource setText(String text) {
        this.text = text;
        return this;
    }

    public TxtMode getTxtMode() {
        return txtMode;
    }

    public QrResource setTxtMode(TxtMode txtMode) {
        this.txtMode = txtMode;
        if (txtMode == TxtMode.ORDER) indexCnt = new AtomicInteger(0);
        return this;
    }
    public QrResource setFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }
    public QrResource setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
        return this;
    }

    public Font getFont(int size) {
        if (fontName == null) fontName = QuickQrUtil.DEFAULT_FONT_NAME;
        return QuickQrUtil.font(fontName, fontStyle, size);
    }

    public AtomicInteger getIndexCnt() {
        return indexCnt;
    }

    public QrResource setIndexCnt(AtomicInteger indexCnt) {
        this.indexCnt = indexCnt;
        return this;
    }

    public String getSvg() {
        return svg;
    }

    public QrResource setSvg(String svg) {
        this.svg = svg;
        return this;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public QrResource setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        return this;
    }

    public PicStyle getPicStyle() {
        return picStyle;
    }

    public QrResource setPicStyle(PicStyle picStyle) {
        this.picStyle = picStyle;
        return this;
    }

    public BufferedImage processImg() {
        return picStyle.process(img, (int) (Math.min(img.getWidth(), img.getHeight()) * cornerRadius));
    }
}
