package com.github.hui.quick.plugin.qrcode.v3.templates.svg;

class RectSvgTag extends SvgTag {
    @Override
    public String toSvgTag() {
        return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\"/>";
    }
}