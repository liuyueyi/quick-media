package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

import com.github.hui.quick.plugin.base.awt.ColorUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SvgTemplate {
    private int width;
    private int height;

    /**
     * 生成的svg tag标签
     */
    private final List<SvgTag> tagList;

    /**
     * 预定义的绘图资源，供其他的symbol进行引用
     */
    private String defs;

    /**
     * svg symbol 标识
     */
    private final Set<String> svgSymbols;

    /**
     * 实时保存渲染颜色
     */
    private String currentColor;

    private SvgTemplate() {
        tagList = new ArrayList<>();
        svgSymbols = new HashSet<>();
        defs = "";
    }


    public SvgTemplate(int width, int height) {
        this();
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public SvgTemplate setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public SvgTemplate setHeight(int height) {
        this.height = height;
        return this;
    }

    public String getDefs() {
        return defs;
    }

    public SvgTemplate setDefs(String defs) {
        this.defs = defs;
        return this;
    }

    public SvgTemplate addTag(SvgTag tag) {
        tagList.add(tag);
        return this;
    }

    public SvgTemplate addSymbol(String svg) {
        if (svg != null) svgSymbols.add(svg);
        return this;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public SvgTemplate setCurrentColor(Color currentColor) {
        if (currentColor != null) this.currentColor = ColorUtil.color2htmlColor(currentColor);
        else this.currentColor = null;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        builder.append("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" ").append("width=\"").append(width).append("\" height=\"").append(height).append("\"").append(" viewBox=\"0 0 ").append(width).append(" ").append(height).append("\">\n");
        builder.append(defs).append("\n");
        for (String symbol : svgSymbols) {
            builder.append("\t").append(symbol).append("\n");
        }

        builder.append("\t<g id=\"qr\">\n");
        for (SvgTag tag : tagList) {
            builder.append("\t\t").append(tag.toString()).append("\n");
        }
        builder.append("\n\t</g>\n</svg>");
        return builder.toString();
    }
}