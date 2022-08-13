package com.github.hui.quick.plugin.base.awt;

import com.github.hui.quick.plugin.base.NumUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

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
        // new Color(color, true);
        int a = (0xff000000 & color) >>> 24;
        int r = (0x00ff0000 & color) >> 16;
        int g = (0x0000ff00 & color) >> 8;
        int b = (0x000000ff & color);
        return new Color(r, g, b, a);
    }

    /**
     * html 形如 #ffeecc11 格式的颜色转换为jdk的Color对象
     * #issues 104
     *
     * @param color
     * @return
     */
    public static Color html2color(String color) {
        if (!color.startsWith("#")) {
            throw new IllegalArgumentException("html color may be #ffeecc11, but you put:" + color);
        }
        int r, g, b, a;
        color = color.substring(1);
        if (color.length() == 3) {
            // #rgb 格式的颜色处理
            r = NumUtil.decode2int(String.format("0x%s%s", color.charAt(0), color.charAt(0)), 255);
            g = NumUtil.decode2int(String.format("0x%s%s", color.charAt(1), color.charAt(1)), 255);
            b = NumUtil.decode2int(String.format("0x%s%s", color.charAt(2), color.charAt(2)), 255);
            a = 255;
        } else if (color.length() == 4) {
            // #rgba 格式
            r = NumUtil.decode2int(String.format("0x%s%s", color.charAt(0), color.charAt(0)), 255);
            g = NumUtil.decode2int(String.format("0x%s%s", color.charAt(1), color.charAt(1)), 255);
            b = NumUtil.decode2int(String.format("0x%s%s", color.charAt(2), color.charAt(2)), 255);
            a = NumUtil.decode2int(String.format("0x%s%s", color.charAt(3), color.charAt(3)), 255);
        } else if (color.length() == 6) {
            // #rrggbb 格式
            r = NumUtil.decode2int("0x" + color.substring(0, 2), 255);
            g = NumUtil.decode2int("0x" + color.substring(2, 4), 255);
            b = NumUtil.decode2int("0x" + color.substring(4, 6), 255);
            a = 255;
        } else if (color.length() == 8) {
            r = NumUtil.decode2int("0x" + color.substring(0, 2), 255);
            g = NumUtil.decode2int("0x" + color.substring(2, 4), 255);
            b = NumUtil.decode2int("0x" + color.substring(4, 6), 255);
            a = NumUtil.decode2int("0x" + color.substring(6, 8), 255);
        } else {
            throw new IllegalArgumentException("illegal color format for: " + color);
        }
        return new Color(r, g, b, a);
    }


    /**
     * 字符串格式颜色转Color，支持html、十六进制、十进制转换
     *
     * @param color
     * @return
     */
    public static Color str2color(String color) {
        if (StringUtils.isBlank(color)) return null;
            // html格式颜色转换
        else if (color.startsWith("#")) return ColorUtil.html2color(color);
            // 十六进制颜色
        else if (color.startsWith("0x")) return ColorUtil.int2color(NumUtil.decode2int(color, null));
            // 整数类型
        else if (NumberUtils.isDigits(color)) return ColorUtil.int2color(Integer.parseInt(color));
        throw new IllegalArgumentException("illegal color value: " + color);
    }

    /**
     * 将Color对象转为html对应的颜色配置信息
     * <p>
     * 如  Color.RED  ->  #f00
     *
     * @param color
     * @return
     */
    public static String int2htmlColor(int color) {
        int a = (0xff000000 & color) >>> 24;
        int r = (0x00ff0000 & color) >> 16;
        int g = (0x0000ff00 & color) >> 8;
        int b = (0x000000ff & color);
        return "#" + NumUtil.toHex(r) + NumUtil.toHex(g) + NumUtil.toHex(b) + NumUtil.toHex(a);
    }

    /**
     * 转html对应的颜色配置
     *
     * @param color
     * @return
     */
    public static String color2htmlColor(Color color) {
        return "#" + NumUtil.toHex(color.getRed()) + NumUtil.toHex(color.getGreen()) + NumUtil.toHex(color.getBlue()) + NumUtil.toHex(color.getAlpha());
    }
}
