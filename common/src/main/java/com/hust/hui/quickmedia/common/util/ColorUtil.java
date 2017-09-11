package com.hust.hui.quickmedia.common.util;

import java.awt.*;

/**
 * Created by yihui on 2017/7/27.
 */
public class ColorUtil {

    /**
     * 全透明颜色
     */
    public static Color OPACITY = ColorUtil.int2color(0x00FFFFFF);


    /**
     * 米黄色
     */
    public static Color OFF_WHITE = ColorUtil.int2color(0xFFF7EED6);


    /**
     * int格式的颜色转为 awt 的Color对象
     *
     * @param color 0xffffffff  前两位为透明读， 三四位 R， 五六位 G， 七八位 B
     * @return
     */
    public static Color int2color(int color) {
        int a = ((0x7f000000 & color) >> 24) | 0x00000080;
        int r = (0x00ff0000 & color) >> 16;
        int g = (0x0000ff00 & color) >> 8;
        int b = (0x000000ff & color);
        return new Color(r, g, b, a);
    }


    /**
     * 将Color对象转为html对应的颜色配置信息
     *
     * 如  Color.RED  ->  #f00
     *
     * @param color
     * @return
     */
    public static String int2htmlColor(int color) {
        int r = (0x00ff0000 & color) >> 16;
        int g = (0x0000ff00 & color) >> 8;
        int b = (0x000000ff & color);
        return "#" + NumUtil.toHex(r) + NumUtil.toHex(g) + NumUtil.toHex(b);
    }
}
