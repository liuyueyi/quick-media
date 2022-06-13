package com.github.hui.quick.plugin.qrcode.v3.templates.svg;

import com.github.hui.quick.plugin.base.ColorUtil;

/**
 * @author yihui
 * @date 2022/6/10
 */
public class TextSvgTag extends SvgTag {
    private String text;

    public String getText() {
        return text;
    }

    public TextSvgTag setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public String toSvgTag() {
        return String.format("<tspan x=\"%s\" y=\"%s\" dw=\"%s\" dy=\"%s\" style=\"fill:%s\" xml:space=\"preserve\">%s</tspan>", x, y, w, h, ColorUtil.color2htmlColor(color), text);
    }
}
