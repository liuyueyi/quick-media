package com.github.hui.quick.plugin.qrcode.v3.req;

import com.github.hui.quick.plugin.qrcode.helper.QrCodeRenderHelper;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;

import java.awt.*;

/**
 * 探测图形的配置信息
 */
public class DetectOptions {
    private QrCodeV3Options options;

    /**
     * 外层颜色
     */
    private Color outColor;

    /**
     * 内层颜色
     */
    private Color inColor;
    /**
     * 兜底的资源
     */
    private QrResource resource;
    /**
     * 左上角的探测图形
     */
    private QrResource lt;
    /**
     * 右上角的探测图形
     */
    private QrResource rt;
    /**
     * 左下角的探测图形
     */
    private QrResource ld;
    /**
     * true 表示探测图形单独处理
     * false 表示探测图形的样式更随二维码的主样式
     */
    private Boolean special;

    /**
     * true 表示一个探测图形只用一个资源进行填充；适用于指定探测图形图片、svg节点的场景
     */
    private Boolean whole;

    public DetectOptions(QrCodeV3Options options) {
        this.options = options;
    }

    public Color getOutColor() {
        return outColor;
    }

    public DetectOptions setOutColor(Color outColor) {
        this.outColor = outColor;
        return this;
    }

    public Color getInColor() {
        return inColor;
    }

    public DetectOptions setInColor(Color inColor) {
        this.inColor = inColor;
        return this;
    }

    public QrResource getLt() {
        return lt;
    }

    public DetectOptions setLt(QrResource lt) {
        this.lt = lt;
        this.resource = lt;
        return this;
    }

    public QrResource getRt() {
        return rt;
    }

    public DetectOptions setRt(QrResource rt) {
        this.rt = rt;
        this.resource = rt;
        return this;
    }

    public QrResource getLd() {
        return ld;
    }

    public DetectOptions setLd(QrResource ld) {
        this.ld = ld;
        this.resource = ld;
        return this;
    }

    public QrResource getResource() {
        return resource;
    }

    public DetectOptions setResource(QrResource resource) {
        this.resource = resource;
        return this;
    }

    public Boolean getSpecial() {
        return special;
    }

    public DetectOptions setSpecial(Boolean special) {
        this.special = special;
        return this;
    }

    public Boolean getWhole() {
        return whole;
    }

    public DetectOptions setWhole(Boolean whole) {
        this.whole = whole;
        return this;
    }

    public QrResource chooseDetectResource(QrCodeRenderHelper.DetectLocation detectLocation) {
        switch (detectLocation) {
            case LD:
                return ld == null ? resource : ld;
            case LT:
                return lt == null ? resource : lt;
            case RT:
                return rt == null ? resource : rt;
            default:
                return null;
        }
    }

    public QrCodeV3Options complete() {
        if (special == null) special = false;
        return options;
    }
}