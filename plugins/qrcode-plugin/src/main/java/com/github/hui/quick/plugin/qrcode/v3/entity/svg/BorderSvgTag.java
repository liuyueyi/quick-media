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

    protected String fillColor = "#ffffff";

    public int getRoundRate() {
        return roundRate;
    }

    public BorderSvgTag setRoundRate(int roundRate) {
        this.roundRate = roundRate;
        return this;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public BorderSvgTag setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public String getFillColor() {
        return fillColor;
    }

    public BorderSvgTag setFillColor(String fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    @Override
    public String toString() {
        int round = Math.floorDiv(w, roundRate);
        return "<rect" + " style=\"stroke:" + color + ";fill:" + fillColor + "; stroke-width:" + strokeWidth +
                "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\" rx=\"" + round + "\" ry=\"" + round + "\" />";
    }
}
