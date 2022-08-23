package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

import com.github.hui.quick.plugin.qrcode.util.NumUtil;

/**
 * 八边形器
 * @author YiHui
 */
public class OctagonSvgTag extends SvgTag {
    @Override
    public String toString() {
        float rate = NumUtil.divWithScaleFloor(w, 30, 2);
        StringBuilder points = new StringBuilder()
                .append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(",").append(y).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(30, rate, 2)).append(" ")
                .append(x).append(",").append(y + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(" ")
                .append(x).append(",").append(y + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(" ");

        return "<polygon points=\"" + points + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
    }
}