package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

public class SvgTag {
    protected String color;
    protected int x;
    protected int y;
    protected int w;
    protected int h;

    public String getColor() {
        return color;
    }

    public SvgTag setColor(String color) {
        this.color = color;
        return this;
    }

    public int getX() {
        return x;
    }

    public SvgTag setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public SvgTag setY(int y) {
        this.y = y;
        return this;
    }

    public int getW() {
        return w;
    }

    public SvgTag setW(int w) {
        this.w = w;
        return this;
    }

    public int getH() {
        return h;
    }

    public SvgTag setH(int h) {
        this.h = h;
        return this;
    }
}