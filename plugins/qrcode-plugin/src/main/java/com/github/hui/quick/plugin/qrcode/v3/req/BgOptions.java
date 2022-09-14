package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;

import java.awt.image.BufferedImage;
import java.util.Optional;

/**
 * 背景图的配置信息
 *
 * @author YiHui
 */
public class BgOptions {
    private final QrCodeV3Options options;
    /**
     * 背景图
     */
    private QrResource bg;

    /**
     * 背景图宽
     */
    private int bgW;

    /**
     * 背景图高
     */
    private int bgH;

    /**
     * 背景图样式，默认为 OVERRIDE
     */
    private BgStyle bgStyle;

    /**
     * if {@link #bgStyle} ==  BgStyle.OVERRIDE，
     * 用于设置二维码的透明度 0 - 1，越小则透明度越高，
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

    public BgOptions setBg(String bg) {
        return setBg(new QrResource(bg));
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
        // 背景图宽高
        if (bg != null) {
            BufferedImage img = Optional.ofNullable(bg.getImg()).orElse(bg.getGif() != null ? bg.getGif().getImage() : null);
            Optional.ofNullable(img).ifPresent(i -> {
                if (bgW <= 0) bgW = img.getWidth();
                if (bgH <= 0) bgH = img.getHeight();
            });
        }

        // 默认采用全覆盖方式
        if (bgStyle == null) bgStyle = BgStyle.OVERRIDE;

        if (bgStyle == BgStyle.OVERRIDE && opacity <= 0) {
            // 默认将覆盖方式的二维码透明设置为0.8
            opacity = 0.85F;
        }

        if (opacity > 1) opacity = 1.0F;

        return options;
    }
}
