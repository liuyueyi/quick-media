package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 普通的二维码
 */
public class RectSvgTag extends SvgTag {
    @Override
    public String toString() {
        return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
    }
}
