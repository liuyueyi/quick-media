package com.github.hui.quick.plugin.image.wrapper.split;

import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.image.wrapper.split.util.ImgSplitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author YiHui
 * @date 2023/1/9
 */
public class ImgSplitWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(ImgSplitWrapper.class);

    private final ImgSplitOptions imgSplitOptions;


    public static Builder build() {
        return new Builder();
    }

    private ImgSplitWrapper(ImgSplitOptions imgSplitOptions) {
        this.imgSplitOptions = imgSplitOptions;
    }

    public void splitAndSave() throws IOException {
        List<BufferedImage> imgs = ImgSplitUtil.split(imgSplitOptions.getImg());
        int i = imgSplitOptions.getOutputIncrIndex();
        for (BufferedImage bi : imgs) {
            ImageIO.write(bi, "png", new File(imgSplitOptions.getOutputPath() + "/" + imgSplitOptions.getOutputName() + "_" + i + ".png"));
            i++;
        }
    }

    public List<BufferedImage> split2imgs() {
        return ImgSplitUtil.split(imgSplitOptions.getImg());
    }

    public static class Builder {
        private ImgSplitOptions options;

        public Builder() {
            this.options = new ImgSplitOptions();
        }

        public Builder setImg(String img) throws IOException {
            options.setImg(ImageLoadUtil.getImageByPath(img));
            return this;
        }

        public Builder setOutputPath(String output) {
            options.setOutputPath(output);
            return this;
        }

        public Builder setOutputImgName(String name) {
            options.setOutputName(name);
            return this;
        }

        public Builder setOutputIncrIndex(int index) {
            options.setOutputIncrIndex(index);
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
            return new ImgSplitWrapper(options);
        }
    }
}
