package com.github.hui.quick.plugin.image.wrapper.svg;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * jpg/png -> svg的配置信息
 *
 * @author YiHui
 * @date 2023/1/16
 */
public class SvgParserOptions {
    /**
     * 原始图
     */
    private BufferedImage source;

    /**
     * 对于转字符图时，它控制字符大小
     * 对于灰度/像素处理时，这个表示像素化的处理操作
     */
    private int blockSize;

    /**
     * 缩放比例，1 表示输出的图不缩放； > 1，表示生成的图，按倍数扩大
     */
    private Double scaleRate;

    /**
     * 背景色判断方式，传参为intColor, 如果返回true，表示认定为背景色；否则不是
     */
    private Predicate<Color> bgPredicate;

    public BufferedImage getSource() {
        return source;
    }

    public void setSource(BufferedImage source) {
        this.source = source;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public Double getScaleRate() {
        return scaleRate;
    }

    public void setScaleRate(Double scaleRate) {
        this.scaleRate = scaleRate;
    }

    public Predicate<Color> getBgPredicate() {
        return bgPredicate;
    }

    public void setBgPredicate(Predicate<Color> bgPredicate) {
        this.bgPredicate = bgPredicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SvgParserOptions that = (SvgParserOptions) o;
        return blockSize == that.blockSize && Objects.equals(source, that.source) && Objects.equals(scaleRate, that.scaleRate) && Objects.equals(bgPredicate, that.bgPredicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, blockSize, scaleRate, bgPredicate);
    }

    @Override
    public String toString() {
        return "SvgParserOptions{" +
                "source=" + source +
                ", blockSize=" + blockSize +
                ", scaleRate=" + scaleRate +
                ", bgPredicate=" + bgPredicate +
                '}';
    }
}
