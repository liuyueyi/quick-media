package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

/**
 * 圆角矩形
 *
 * @author YiHui
 * @date 2022/9/7
 */
public class RoundRectSvgTag extends SvgTag {
    /**
     * 圆角比例, 默认 = 4
     */
    protected int roundRate = 4;


    public int getRoundRate() {
        return roundRate;
    }

    public RoundRectSvgTag setRoundRate(int roundRate) {
        this.roundRate = roundRate;
        return this;
    }

    @Override
    public String toString() {
        int round = Math.floorDiv(w, roundRate);
        return "<rect" + " fill=\"" + color + "\" height=\"" + h + "\" width=\"" + w + "\" y=\"" + y + "\" x=\"" + x + "\" rx=\"" + round + "\" ry=\"" + round + "\" />";
    }
}
