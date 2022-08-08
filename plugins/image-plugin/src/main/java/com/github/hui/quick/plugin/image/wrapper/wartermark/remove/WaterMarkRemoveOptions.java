package com.github.hui.quick.plugin.image.wrapper.wartermark.remove;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.NumUtil;
import com.github.hui.quick.plugin.image.wrapper.wartermark.remove.operator.WaterMarkRemoveTypeEnum;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * @author YiHui
 * @date 2022/7/13
 */
public class WaterMarkRemoveOptions {
    private WaterMarkRemoveWrapper waterMarkRemoveWrapper;

    public WaterMarkRemoveOptions(WaterMarkRemoveWrapper waterMarkRemoveWrapper) {
        this.waterMarkRemoveWrapper = waterMarkRemoveWrapper;
    }


    /**
     * 图片地址, 也可以是base64格式的图片
     */
    private BufferedImage img;
    /**
     * 水印的起始x坐标，坐标系从左上角出发
     */
    private Integer waterMarkX;
    /**
     * 水印的起始y坐标，坐标系从左上角出发
     */
    private Integer waterMarkY;
    /**
     * 水印宽
     */
    private Integer waterMarkW;
    /**
     * 水印高
     */
    private Integer waterMarkH;
    /**
     * @see WaterMarkRemoveTypeEnum#getType()
     */
    private String type;
    /**
     * 水印填充颜色
     */
    private Color fillColor;
    /**
     * 像素大小：默认为10，适用于像素方式移除水印的场景
     */
    private Integer pixelSize;

    /**
     * 基于背景色自动填充的场景参数，将水印拆分为上下两部分，这个参数指定拆分比例，如 0.5 表示上下部分各占一半（默认值），如 0.1 则表示上部分占整体水印的10%；1则表示全部为上部分
     */
    private Float upDownRate = 0.5F;

    /**
     * 基于背景色自动填充时，指定上部分水印，选取的背景区域，如1，表示选择与水印上部分等高的背景色进行填充，2则表示取两倍上部分范围；0.5为上部分的一半
     */
    private Float upRange;

    /**
     * 基于背景色自动填充时，指定下部分水印，选取的背景区域，如1，表示选择与水印下部分等高的背景色进行填充，2则表示取两倍下部分范围；0.5为下部分的一半
     */
    private Float downRange;

    private String imgType;

    public BufferedImage getImg() {
        return img;
    }

    public WaterMarkRemoveOptions setImg(BufferedImage img) {
        this.img = img;
        return this;
    }

    public Integer getWaterMarkX() {
        return waterMarkX;
    }

    public WaterMarkRemoveOptions setWaterMarkX(Integer waterMarkX) {
        this.waterMarkX = waterMarkX;
        return this;
    }

    public Integer getWaterMarkY() {
        return waterMarkY;
    }

    public WaterMarkRemoveOptions setWaterMarkY(Integer waterMarkY) {
        this.waterMarkY = waterMarkY;
        return this;
    }

    public Integer getWaterMarkW() {
        return waterMarkW;
    }

    public WaterMarkRemoveOptions setWaterMarkW(Integer waterMarkW) {
        this.waterMarkW = waterMarkW;
        return this;
    }

    public Integer getWaterMarkH() {
        return waterMarkH;
    }

    public WaterMarkRemoveOptions setWaterMarkH(Integer waterMarkH) {
        this.waterMarkH = waterMarkH;
        return this;
    }

    public String getType() {
        return type;
    }

    public WaterMarkRemoveOptions setType(String type) {
        this.type = type;
        return this;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public WaterMarkRemoveOptions setFillColor(String fillColor) {
        int c = NumUtil.decode2int(fillColor, 0xffffffff);
        this.fillColor = ColorUtil.int2color(c);
        return this;
    }

    public WaterMarkRemoveOptions setFillColor(Color color) {
        this.fillColor = color;
        return this;
    }

    public Integer getPixelSize() {
        return pixelSize;
    }

    public WaterMarkRemoveOptions setPixelSize(Integer pixelSize) {
        this.pixelSize = pixelSize;
        return this;
    }

    public float getUpDownRate() {
        return upDownRate;
    }

    public WaterMarkRemoveOptions setUpDownRate(Float upDownRate) {
        this.upDownRate = upDownRate;
        return this;
    }

    public Float getUpRange() {
        return upRange;
    }

    public WaterMarkRemoveOptions setUpRange(Float upRange) {
        this.upRange = upRange;
        return this;
    }

    public Float getDownRange() {
        return downRange;
    }

    public WaterMarkRemoveOptions setDownRange(Float downRange) {
        this.downRange = downRange;
        return this;
    }

    public String getImgType() {
        return imgType;
    }

    public WaterMarkRemoveOptions setImgType(String imgType) {
        this.imgType = imgType;
        return this;
    }

    public WaterMarkRemoveWrapper build() {
        if (upDownRate == null) upDownRate = 0.5F;
        else if (upDownRate > 1) upDownRate = 1F;
        else if (upDownRate < 0) upDownRate = 0F;
        if (upRange == null) upRange = 1F;
        if (downRange == null) downRange = 1F;
        if (fillColor == null) fillColor = Color.WHITE;
        if (type == null) type = WaterMarkRemoveTypeEnum.BG.getType();
        if (pixelSize == null) pixelSize = 10;

        if (imgType == null) {
            if (this.img.getColorModel().getTransparency() == Transparency.TRANSLUCENT) {
                imgType = "png";
            } else {
                imgType = "jpg";
            }
        }
        return waterMarkRemoveWrapper;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WaterMarkRemoveOptions options = (WaterMarkRemoveOptions) o;
        return Objects.equals(img, options.img) && Objects.equals(waterMarkX, options.waterMarkX) && Objects.equals(waterMarkY, options.waterMarkY) && Objects.equals(waterMarkW, options.waterMarkW) && Objects.equals(waterMarkH, options.waterMarkH) && Objects.equals(type, options.type) && Objects.equals(fillColor, options.fillColor) && Objects.equals(pixelSize, options.pixelSize) && Objects.equals(upDownRate, options.upDownRate) && Objects.equals(upRange, options.upRange) && Objects.equals(downRange, options.downRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(img, waterMarkX, waterMarkY, waterMarkW, waterMarkH, type, fillColor, pixelSize, upDownRate, upRange, downRange);
    }

    @Override
    public String toString() {
        return "WaterMarkRemoveOptions{" + "img=" + img + ", waterMarkX=" + waterMarkX + ", waterMarkY=" + waterMarkY + ", waterMarkW=" + waterMarkW + ", waterMarkH=" + waterMarkH + ", type='" + type + '\'' + ", fillColor=" + fillColor + ", pixelSize=" + pixelSize + ", upDownRate=" + upDownRate + ", upRange=" + upRange + ", downRange=" + downRange + '}';
    }
}
