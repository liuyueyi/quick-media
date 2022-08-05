package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

import com.github.hui.quick.plugin.qrcode.util.NumUtil;

/**
 * 五角星
 */
public class StarSvgTag extends SvgTag {
    @Override
    public String toString() {
        float rate = NumUtil.divWithScaleFloor(w, 20, 2);
        StringBuilder points = new StringBuilder();
        points.append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y + rate).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(4, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(19.8f, rate, 2)).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(19, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(7.8f, rate, 2)).append(" ")
                .append(x + rate).append(",").append(y + NumUtil.multiplyWithScaleFloor(7.8f, rate, 2)).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(16, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(19.8f, rate, 2)).append(" ");

        return "<polygon points=\"" + points + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
    }
}