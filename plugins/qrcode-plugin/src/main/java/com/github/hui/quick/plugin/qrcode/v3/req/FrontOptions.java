package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 前置图的配置信息
 * @author YiHui
 */
public class FrontOptions {
    private final QrCodeV3Options options;

    /**
     * 前置图
     */
    private QrResource ft;

    /**
     * 前置图宽
     */
    private int ftW;

    /**
     * 前置图高
     */
    private int ftH;

    /**
     * 用于设置二维码的绘制在前置图上的x坐标
     */
    private int startX;


    /**
     * 用于设置二维码的绘制在前置图上的y坐标
     */
    private int startY;

    /**
     * 二维码大小比最终输出的图片小时，用来指定二维码周边的填充色，不存在时，默认透明
     */
    private Color fillColor;

    public FrontOptions(QrCodeV3Options options) {
        this.options = options;
    }

    public QrResource getFt() {
        return ft;
    }

    public FrontOptions setFt(QrResource ft) {
        this.ft = ft;
        return this;
    }

    public FrontOptions setFt(String ft) {
        this.ft = new QrResource(ft);
        return this;
    }

    public int getFtW() {
        return ftW;
    }

    public FrontOptions setFtW(int ftW) {
        this.ftW = ftW;
        return this;
    }

    public int getFtH() {
        return ftH;
    }

    public FrontOptions setFtH(int ftH) {
        this.ftH = ftH;
        return this;
    }

    public int getStartX() {
        return startX;
    }

    public FrontOptions setStartX(int startX) {
        this.startX = startX;
        return this;
    }

    public int getStartY() {
        return startY;
    }

    public FrontOptions setStartY(int startY) {
        this.startY = startY;
        return this;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public FrontOptions setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public QrCodeV3Options complete() {
        if (ft != null) {
            BufferedImage rImg = ft.getImg() != null ? ft.getImg() : (ft.getGif() != null ? ft.getGif().getImage() : null);
            if (rImg != null) {
                if (ftW <= 0) ftW = rImg.getWidth();
                if (ftH <= 0) ftH = rImg.getHeight();
            }
        }

        return options;
    }
}
