package com.github.hui.quick.plugin.qrcode.v3.options;

import java.awt.*;

/**
 * @author
 * @date 2022/6/10
 */
public class SourceOptions {
    private Color color;

    private Font font;

    private Integer fontSize;

    public Color getColor() {
        return color;
    }

    public SourceOptions setColor(Color color) {
        this.color = color;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public SourceOptions setFont(Font font) {
        this.font = font;
        return this;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public SourceOptions setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }
}
