package com.github.hui.quick.plugin.image.wrapper.split;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author YiHui
 * @date 2023/1/9
 */
public class ImgSplitOptions {
    /**
     * 原图路径
     */
    private BufferedImage img;

    /**
     * 输出路径
     */
    private String outputPath;

    /**
     * 输出文件名
     */
    private String outputName;

    /**
     * 输出文件名的起始下标
     */
    private Integer outputIncrIndex;

    /**
     * 背景色判断方式，传参为intColor, 如果返回true，表示认定为背景色；否则不是
     */
    private Predicate<Integer> bgPredicate;

    public ImgSplitOptions() {
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputName() {
        return outputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }

    public Integer getOutputIncrIndex() {
        return outputIncrIndex;
    }

    public void setOutputIncrIndex(Integer outputIncrIndex) {
        this.outputIncrIndex = outputIncrIndex;
    }

    public Predicate<Integer> getBgPredicate() {
        return bgPredicate;
    }

    public void setBgPredicate(Predicate<Integer> bgPredicate) {
        this.bgPredicate = bgPredicate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImgSplitOptions that = (ImgSplitOptions) o;
        return Objects.equals(img, that.img) && Objects.equals(outputPath, that.outputPath) && Objects.equals(outputName, that.outputName) && Objects.equals(outputIncrIndex, that.outputIncrIndex) && Objects.equals(bgPredicate, that.bgPredicate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(img, outputPath, outputName, outputIncrIndex, bgPredicate);
    }

    @Override
    public String toString() {
        return "ImgSplitOptions{" +
                "img=" + img +
                ", outputPath='" + outputPath + '\'' +
                ", outputName='" + outputName + '\'' +
                ", outputIncrIndex=" + outputIncrIndex +
                ", bgPredicate=" + bgPredicate +
                '}';
    }
}
