package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

public class CircleSvgTag extends SvgTag {
    @Override
    public String toString() {
        int r = Math.floorDiv(w, 2);
        x += r;
        y += r;
        return "<circle cx=\"" + x + "\" cy=\"" + y + "\" r=\"" + r + "\" fill=\"" + color + "\"/>";
    }
}







