package com.github.hui.quick.plugin.image.wrapper.split;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.base.file.FileWriteUtil;
import com.github.hui.quick.plugin.image.wrapper.split.util.ImgSplitUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author YiHui
 * @date 2023/1/9
 */
public class ImgSplitWrapper {
    private final ImgSplitOptions imgSplitOptions;


    public static Builder build() {
        return new Builder();
    }

    private ImgSplitWrapper(ImgSplitOptions imgSplitOptions) {
        this.imgSplitOptions = imgSplitOptions;
    }

    public void splitAndSave() throws IOException {
        List<BufferedImage> imgs = ImgSplitUtil.split(imgSplitOptions.getImg(), imgSplitOptions.getBgPredicate());
        int i = imgSplitOptions.getOutputIncrIndex();
        for (BufferedImage bi : imgs) {
            FileWriteUtil.mkDir(new File(imgSplitOptions.getOutputPath()));
            ImageIO.write(bi, "png", new File(imgSplitOptions.getOutputPath() + "/" + imgSplitOptions.getOutputName() + "_" + i + ".png"));
            i++;
        }
    }

    public List<BufferedImage> split2imgs() {
        return ImgSplitUtil.split(imgSplitOptions.getImg(), imgSplitOptions.getBgPredicate());
    }

    public static class Builder {
        private final ImgSplitOptions options;

        public Builder() {
            this.options = new ImgSplitOptions();
        }

        /**
         * 设置原图
         *
         * @param img
         * @return
         * @throws IOException
         */
        public Builder setImg(String img) throws IOException {
            options.setImg(ImageLoadUtil.getImageByPath(img));
            return this;
        }

        public Builder setImg(BufferedImage img) {
            options.setImg(img);
            return this;
        }

        /**
         * 设置拆分图片的保存路径
         *
         * @param output
         * @return
         */
        public Builder setOutputPath(String output) {
            options.setOutputPath(output);
            return this;
        }

        /**
         * 设置拆分图片名
         *
         * @param name
         * @return
         */
        public Builder setOutputImgName(String name) {
            options.setOutputName(name);
            return this;
        }

        /**
         * 设置拆分图片的起始序号值，默认从0开始
         *
         * @param index
         * @return
         */
        public Builder setOutputIncrIndex(int index) {
            options.setOutputIncrIndex(index);
            return this;
        }

        /**
         * 设置用来识别背景色的判定方法
         *
         * @param predicate
         * @return
         */
        public Builder setBgPredicate(Predicate<Integer> predicate) {
            options.setBgPredicate(predicate);
            return this;
        }

        public ImgSplitWrapper build() {
            if (options.getOutputPath() == null) {
                options.setOutputPath("/tmp");
            }
            if (options.getOutputName() == null) {
                options.setOutputName(UUID.randomUUID().toString());
            }
            if (options.getOutputIncrIndex() == null) {
                options.setOutputIncrIndex(0);
            }
            if (options.getBgPredicate() == null) {
                options.setBgPredicate(integer -> new Color(integer, true).getAlpha() == 0);
            }
            return new ImgSplitWrapper(options);
        }
    }
}
