package com.github.hui.quick.plugin.qrcode.v3.entity.svg;

import com.github.hui.quick.plugin.qrcode.util.NumUtil;

/**
 * 三角形
 *
 * @author yihui
 */
public class TriangleSvgTag extends SvgTag {
    @Override
    public String toString() {
        float rate = NumUtil.divWithScaleFloor(w, 20, 2);
        StringBuilder points = new StringBuilder();
        points.append(x + NumUtil.multiplyWithScaleFloor(10, rate, 2)).append(",").append(y).append(" ")
                .append(x).append(",").append(y + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(" ")
                .append(x + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(",").append(y + NumUtil.multiplyWithScaleFloor(20, rate, 2)).append(" ");
        return "<polygon points=\"" + points + "\"  style=\"fill:" + color + ";fill-rule:nonzero;\" />";
    }
}







