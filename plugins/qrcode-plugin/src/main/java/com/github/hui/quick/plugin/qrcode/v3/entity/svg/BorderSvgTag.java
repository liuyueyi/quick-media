package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * logo边框样式
 *
 * @author YiHui
 * @date 2022/9/7
 */
public class BorderSvgTag extends SvgTag {
    /**
     * 圆角弧度比例
     */
    protected int roundRate = 8;

    protected int strokeWidth = 2;

    @Override
    public String toString() {
        int round = Math.floorDiv(w, roundRate);
        return "<rect" + " style=\"stroke:" + color + ";fill:#ffffff; stroke-width:" + strokeWidth +
                "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\" rx=\"" + round + "\" ry=\"" + round + "\" />";
    }
}
