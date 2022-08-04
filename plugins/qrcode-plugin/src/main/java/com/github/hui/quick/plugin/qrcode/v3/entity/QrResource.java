package com.github.hui.quick.plugin.qrcode.v3.entity;

import com.github.hui.quick.plugin.base.gif.GifDecoder;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;

import java.awt.image.BufferedImage;

/**
 * @author YiHui
 * @date 2022/7/20
 */
public class QrResource {
    /**
     * 动态背景图
     */
    private GifDecoder gifDecoder;

    /**
     * 背景图
     */
    private BufferedImage img;

    /**
     * 图片的圆角比例，默认为原图的 1/8
     */
    private float cornerRadius = 1 / 8.0f;

    /**
     * 图样式
     */
    private PicStyle picStyle = PicStyle.NORMAL;

    /**
     * 文字
     */
    private String text;

    /**
     * svg tag
     */
    private String svg;


    public GifDecoder getGifDecoder() {
        return gifDecoder;
    }

    public QrResource setGifDecoder(GifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
        return this;
    }

    public BufferedImage getImg() {
        return img;
    }

    public QrResource setImg(BufferedImage img) {
        this.img = img;
        return this;
    }

    public String getText() {
        return text;
    }

    public QrResource setText(String text) {
        this.text = text;
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
