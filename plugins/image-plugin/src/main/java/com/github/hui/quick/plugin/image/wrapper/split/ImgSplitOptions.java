package com.github.hui.quick.plugin.image.wrapper.split;

import java.awt.image.BufferedImage;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImgSplitOptions that = (ImgSplitOptions) o;
        return Objects.equals(img, that.img) && Objects.equals(outputPath, that.outputPath) && Objects.equals(outputName, that.outputName) && Objects.equals(outputIncrIndex, that.outputIncrIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(img, outputPath, outputName, outputIncrIndex);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
