package com.github.hui.quick.plugin.qrcode.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author yihui
 * @date 2022/4/12
 */
public class NumUtil {

    /**
     * 除，并向下取整
     *
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static float divWithScaleFloor(float a, float b, int scale) {
        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), scale, RoundingMode.FLOOR).floatValue();
    }

    /**
     * 乘，并向上取整
     *
     * @param a
     * @param b
     * @param scale
     * @return
     */
    public static float multiplyWithScaleFloor(float a, float b, int scale) {
        return BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).setScale(scale, RoundingMode.FLOOR).floatValue();
    }
}
