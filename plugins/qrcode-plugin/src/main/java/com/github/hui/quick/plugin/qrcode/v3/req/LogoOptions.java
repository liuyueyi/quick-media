package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;

import java.awt.*;

/**
 * logo 的配置信息
 */
public class LogoOptions {
    private QrCodeV3Options options;

    /**
     * logo 图片
     */
    private QrResource logo;

    /**
     * logo 占二维码的比例， rate 要求 > 4
     */
    private int rate;

    /**
     * 用于设置logo的透明度
     */
    private Float opacity;

    /**
     * 当存在logo时，默认值为true
     * <p>
     * true 表示将logo区域的二维码移除掉
     * false logo区域的二维码不做任何处理
     */
    private Boolean clearLogoArea;

    /**
     * 边框颜色
     */
    private Color borderColor;

    /**
     * 外围边框颜色
     */
    private Color outerBorderColor;

    public LogoOptions(QrCodeV3Options options) {
        this.options = options;
    }

    public QrResource getLogo() {
        return logo;
    }

    public LogoOptions setLogo(QrResource logo) {
        this.logo = logo;
        return this;
    }

    public int getRate() {
        return rate;
    }

    public LogoOptions setRate(int rate) {
        this.rate = rate;
        return this;
    }

    public Float getOpacity() {
        return opacity;
    }

    public LogoOptions setOpacity(Float opacity) {
        this.opacity = opacity;
        return this;
    }

    public Boolean getClearLogoArea() {
        return clearLogoArea;
    }

    public LogoOptions setClearLogoArea(boolean clearLogoArea) {
        this.clearLogoArea = clearLogoArea;
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public LogoOptions setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public Color getOuterBorderColor() {
        return outerBorderColor;
    }

    public LogoOptions setOuterBorderColor(Color outerBorderColor) {
        this.outerBorderColor = outerBorderColor;
        return this;
    }

    public QrCodeV3Options complete() {
        if (rate <= 4) rate = 10;
        if (logo != null && clearLogoArea == null) clearLogoArea = true;

        return this.options;
    }
}
