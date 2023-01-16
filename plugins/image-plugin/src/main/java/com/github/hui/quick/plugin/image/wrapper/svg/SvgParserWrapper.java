package com.github.hui.quick.plugin.image.wrapper.svg;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.image.helper.ImgPixelHelper;
import com.github.hui.quick.plugin.image.util.StrUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;

import static com.github.hui.quick.plugin.image.util.PredicateUtil.conditionGetOrElse;

/**
 * 图片转svg
 *
 * @author YiHui
 * @date 2023/1/16
 */
public class SvgParserWrapper {
    private SvgParserOptions options;

    private SvgParserWrapper(SvgParserOptions options) {
        this.options = options;
    }

    public static Builder of(String img) throws IOException {
        return new Builder().setSource(img);
    }

    /**
     * 输出svg字符串
     *
     * @return
     */
    public String asString() {
        BufferedImage source = options.getSource();
        if (Math.abs(options.getScaleRate() - 1) > 0.05f) {
            source = GraphicUtil.scaleImg((int) (source.getWidth() * options.getScaleRate()), (int) (source.getHeight() * options.getScaleRate()), source);
        }

        // 输出svg的视图大小
        int zoom = 1;
        int picWidth = source.getWidth();
        int picHeight = source.getHeight();
        int blockSize = options.getBlockSize();
        StringBuilder builder = new StringBuilder();
        builder.append(StrUtil.replace(ImgPixelHelper.SIMPLE_SVG_START, "{width}", "" + picWidth * zoom, "{height}", "" + picHeight * zoom, "{BG_COLOR}", "white"));
        for (int x = 0; x < picWidth; x += blockSize) {
            for (int y = 0; y < picHeight; y += blockSize) {
                int[] colors = ImgPixelHelper.getPixels(source, x, y, options.getBlockSize(), options.getBlockSize());
                Color c = ImgPixelHelper.getAverageColor(colors);
                if (!options.getBgPredicate().test(c)) {
                    builder.append(ImgPixelHelper.getSvgCell(c, x * zoom, y * zoom, blockSize * zoom)).append("\n");
                }
            }
        }
        builder.append(ImgPixelHelper.SIMPLE_SVG_END);
        return builder.toString();
    }

    /**
     * 保存到文件中
     *
     * @param fileName
     * @throws IOException
     */
    public void asFile(String fileName) throws IOException {
        String content = asString();
        File file = new File(fileName);
        FileWriteUtil.mkDir(file.getParentFile());
        FileWriteUtil.saveContent(file, content);
    }


    public static class Builder {
        /**
         * 默认的精度
         */
        private static final int DEFAULT_BLOCK_SIZE = 4;
        /**
         * 默认的缩放比例 1 表示不缩放
         */
        private static final double DEFAULT_RATE = 1D;
        private final SvgParserOptions pixelOptions;

        public Builder() {
            this.pixelOptions = new SvgParserOptions();
        }

        /**
         * 待转换的图片
         *
         * @param img
         * @return
         */
        public Builder setSource(String img) throws IOException {
            pixelOptions.setSource(ImageLoadUtil.getImageByPath(img));
            return this;
        }

        /**
         * 待转换的图片
         *
         * @param img
         * @return
         */
        public Builder setSource(BufferedImage img) {
            pixelOptions.setSource(img);
            return this;
        }

        /**
         * 会将 size * size 区域的信息合并为svg中的一个信息点
         * size 越大，精度越小
         *
         * @param size
         * @return
         */
        public Builder setBlockSize(int size) {
            pixelOptions.setBlockSize(size);
            return this;
        }

        /**
         * 设置缩放比例， > 1 表示放大 < 1 表示缩小
         *
         * @param rate
         * @return
         */
        public Builder setScaleRate(double rate) {
            pixelOptions.setScaleRate(rate);
            return this;
        }

        /**
         * 背景色判断表达式
         *
         * @param predicate
         * @return
         */
        public Builder setBgPredicate(Predicate<Color> predicate) {
            pixelOptions.setBgPredicate(predicate);
            return this;
        }

        public SvgParserWrapper build() {
            SvgParserWrapper wrapper = new SvgParserWrapper(pixelOptions);
            pixelOptions.setBlockSize(conditionGetOrElse((s) -> s > 0, pixelOptions.getBlockSize(), DEFAULT_BLOCK_SIZE));
            pixelOptions.setScaleRate(conditionGetOrElse(Objects::nonNull, pixelOptions.getScaleRate(), DEFAULT_RATE));
            pixelOptions.setBgPredicate(conditionGetOrElse(Objects::nonNull, pixelOptions.getBgPredicate(), color -> {
                return color.getAlpha() == 0 || (color.getRed() == 255 && color.getBlue() == 255 && color.getGreen() == 255);
            }));
            return wrapper;
        }
    }
}
