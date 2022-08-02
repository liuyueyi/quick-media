package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;

/**
 * 背景图的配置信息
 */
public class BgOptions {
    private QrCodeV3Options options;
    /**
     * 背景图
     */
    private QrResource bg;

    /**
     * 背景图样式
     */
    private PicStyle imgStyle;

    /**
     * 圆角弧度，默认为宽高中较小值的 1/8
     */
    private float radiusRate;

    /**
     * 背景图宽
     */
    private int bgW;

    /**
     * 背景图高
     */
    private int bgH;

    /**
     * 背景图样式
     */
    private BgStyle bgStyle;

    /**
     * if {@link #bgStyle} ==  BgStyle.OVERRIDE，
     * 用于设置二维码的透明度
     */
    private float opacity;


    /**
     * if {@link #bgStyle} ==  BgStyle.FILL
     * <p>
     * 用于设置二维码的绘制在背景图上的x坐标
     */
    private int startX;


    /**
     * if {@link #bgStyle} ==  BgStyle.FILL
     * <p>
     * 用于设置二维码的绘制在背景图上的y坐标
     */
    private int startY;

    public BgOptions(QrCodeV3Options options) {
        this.options = options;
    }

    public QrResource getBg() {
        return bg;
    }

    public BgOptions setBg(QrResource bg) {
        this.bg = bg;
        return this;
    }

    public PicStyle getImgStyle() {
        return imgStyle;
    }

    public BgOptions setImgStyle(PicStyle imgStyle) {
        this.imgStyle = imgStyle;
        return this;
    }

    public float getRadiusRate() {
        return radiusRate;
    }

    public BgOptions setRadiusRate(float radiusRate) {
        this.radiusRate = radiusRate;
        return this;
    }

    public int getBgW() {
        return bgW;
    }

    public BgOptions setBgW(int bgW) {
        this.bgW = bgW;
        return this;
    }

    public int getBgH() {
        return bgH;
    }

    public BgOptions setBgH(int bgH) {
        this.bgH = bgH;
        return this;
    }

    public BgStyle getBgStyle() {
        return bgStyle;
    }

    public BgOptions setBgStyle(BgStyle bgStyle) {
        this.bgStyle = bgStyle;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }

    public BgOptions setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public int getStartX() {
        return startX;
    }

    public BgOptions setStartX(int startX) {
        this.startX = startX;
        return this;
    }

    public int getStartY() {
        return startY;
    }

    public BgOptions setStartY(int startY) {
        this.startY = startY;
        return this;
    }

    public QrCodeV3Options complete() {
        if (radiusRate <= 0.01) radiusRate = 1 / 8f;
        if (bg != null) {
            
        }

        if (bgW <= 0) {
            if (bg != null && bg.getImg() != null) {
                bgW = bg.getImg().getWidth();
            }
        }


        return options;
    }
}
