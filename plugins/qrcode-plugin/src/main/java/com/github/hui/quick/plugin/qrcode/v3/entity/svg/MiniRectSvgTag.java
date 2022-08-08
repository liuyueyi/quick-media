package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 留一点空隙的矩形框
 */
public class MiniRectSvgTag extends SvgTag {
    @Override
    public String toString() {
        int offsetX = w / 6, offsetY = h / 6;
        w -= offsetX * 2;
        h -= offsetY * 2;
        x += offsetX;
        y += offsetY;
        return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
    }
}