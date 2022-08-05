package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 圆角矩形
 */
public class RoundRectSvgTag extends SvgTag {
    /**
     * 圆角比例, 默认 = 4
     */
    protected int roundRate = 4;

    @Override
    public String toString() {
        int round = Math.floorDiv(w, roundRate);
        return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\" rx=\"" + round + "\" ry=\"" + round + "\" />";
    }
}
