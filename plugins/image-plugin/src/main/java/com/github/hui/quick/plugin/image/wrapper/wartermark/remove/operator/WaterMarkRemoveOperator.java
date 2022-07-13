package com.github.hui.quick.plugin.image.wrapper.wartermark.remove.operator;

import com.github.hui.quick.plugin.image.wrapper.wartermark.remove.WaterMarkRemoveOptions;

import java.awt.image.BufferedImage;

/**
 * @author YiHui
 * @date 2022/7/13
 */
public interface WaterMarkRemoveOperator {
    /**
     * 去水印
     *
     * @param options
     * @return
     */
    BufferedImage remove(WaterMarkRemoveOptions options);

    /**
     * 用于判定当前的去水印操作类是否满足
     *
     * @param type
     * @return
     */
    boolean match(String type);

    /**
     * 优先级，越小优先级越高
     *
     * @return
     */
    default int sort() {
        return 100;
    }

    /**
     * 判断 坐标i,j 是否在水印内
     *
     * @param x
     * @param y
     * @param options
     * @return
     */
    default boolean inWater(int x, int y, WaterMarkRemoveOptions options) {
        return x >= options.getWaterMarkX() && x < options.getWaterMarkX() + options.getWaterMarkW()
                && y >= options.getWaterMarkY() && y < options.getWaterMarkY() + options.getWaterMarkH();
    }

}
