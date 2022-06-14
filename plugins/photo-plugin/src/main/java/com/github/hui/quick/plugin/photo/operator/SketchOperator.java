package com.github.hui.quick.plugin.photo.operator;

import com.github.hui.quick.plugin.photo.options.OperateOptions;
import com.jhlabs.composite.ColorDodgeComposite;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.GrayscaleFilter;
import com.jhlabs.image.InvertFilter;
import com.jhlabs.image.PointFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @author
 * @date 2022/6/14
 */
public class SketchOperator implements PhotoOperator {
    private SketchOperateOptions operateOptions;

    private SketchOperator(SketchOperateOptions sketchOperateOptions) {
        this.operateOptions = sketchOperateOptions;
    }

    @Override
    public BufferedImage operate() {
        BufferedImage src = operateOptions.getImg();
        //图像灰度化
        PointFilter grayScaleFilter = new GrayscaleFilter();
        BufferedImage grayScale = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        grayScaleFilter.filter(src, grayScale);

        //灰度图像反色
        BufferedImage inverted = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        PointFilter invertFilter = new InvertFilter();
        invertFilter.filter(grayScale, inverted);

        //高斯模糊处理
        GaussianFilter gaussianFilter = new GaussianFilter(operateOptions.getRadius());
        BufferedImage gaussianFiltered = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        gaussianFilter.filter(inverted, gaussianFiltered);

        // 灰度图像和高斯模糊反向图混合
        ColorDodgeComposite cdc = new ColorDodgeComposite(operateOptions.getAlpha());
        CompositeContext cc = cdc.createContext(inverted.getColorModel(), grayScale.getColorModel(), null);
        WritableRaster invertedR = gaussianFiltered.getRaster();
        WritableRaster grayScaleR = grayScale.getRaster();
        // 混合之后的就是我们希望的结果
        BufferedImage composite = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        WritableRaster colorDodgedR = composite.getRaster();
        cc.compose(invertedR, grayScaleR, colorDodgedR);
        return composite;
    }

    public static class SketchOperateOptions<T> extends OperateOptions<T> {
        private float radius = 20f;
        private float alpha = 1.0f;

        public SketchOperateOptions(T delegate) {
            super(delegate);
        }

        public SketchOperateOptions<T> setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public SketchOperateOptions<T> setAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public float getRadius() {
            return radius;
        }

        public float getAlpha() {
            return alpha;
        }

        @Override
        public PhotoOperator operator() {
            return new SketchOperator(this);
        }
    }
}
