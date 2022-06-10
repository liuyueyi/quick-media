package com.github.hui.quick.plugin.qrcode.v3.templates.svg;

import java.awt.*;

/**
 * @author yihui
 * @date 2022/6/10
 */
public abstract class SvgTag {
    protected Color color;
    protected int x;
    protected int y;
    protected int w;
    protected int h;

    /**
     * output svg tag
     *
     * @return
     */
    public abstract String toSvgTag();

    public SvgTag setColor(Color color) {
        this.color = color;
        return this;
    }

    public SvgTag setX(int x) {
        this.x = x;
        return this;
    }

    public SvgTag setY(int y) {
        this.y = y;
        return this;
    }

    public SvgTag setW(int w) {
        this.w = w;
        return this;
    }

    public SvgTag setH(int h) {
        this.h = h;
        return this;
    }
}