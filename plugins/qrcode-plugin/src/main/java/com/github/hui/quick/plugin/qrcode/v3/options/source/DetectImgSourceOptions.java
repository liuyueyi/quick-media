package com.github.hui.quick.plugin.qrcode.v3.options.source;

/**
 * @author
 * @date 2022/6/12
 */
public class DetectImgSourceOptions extends DetectSourceOptions {

    private String imgSrc;

    /**
     * 图片的边角弧度 （0 - 1)
     * the corner radian of image (0 - 1)
     * 0 -> corner radian = 90°
     * 1 -> circle image
     */
    protected float radius;

    /**
     * 图片透不明度 （0 - 1）
     * opacity of image (0 - 1)
     * 0 transparency
     * 1 opacity
     */
    private float opacity;

    public String getImgSrc() {
        return imgSrc;
    }

    public DetectImgSourceOptions setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
        return this;
    }

    public float getRadius() {
        return radius;
    }

    public DetectImgSourceOptions setRadius(float radius) {
        this.radius = radius;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }

    public DetectImgSourceOptions setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }
}
