package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 矩形旋转45°
 */
public class RotateRectSvgTag extends SvgTag {
    @Override
    public String toString() {
        int size = w / 2;
        StringBuilder points = new StringBuilder();
        points.append(x + size).append(",").append(y).append(" ")
                .append(x + size * 2).append(",").append(y + size).append(" ")
                .append(x + size).append(",").append(y + size * 2).append(" ")
                .append(x).append(",").append(y + size).append(" ");

        return "<polygon points=\"" + points.toString() + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
    }
}
