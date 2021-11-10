package com.github.hui.quick.plugin.image.wrapper.pixel;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.image.helper.ImgPixelHelper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.IPixelType;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author yihui
 * @date 21/11/8
 */
public class ImgPixelWrapper {

    private ImgPixelOptions pixelOptions;

    static {
        ImageIO.scanForPlugins();
    }

    public static Builder build() {
        return new Builder();
    }

    private ImgPixelWrapper(ImgPixelOptions pixelOptions) {
        this.pixelOptions = pixelOptions;
    }

    /**
     * 转成像素图片
     *
     * @return
     */
    public BufferedImage toImg() {
        if (pixelOptions.getSource() == null) {
            throw new IllegalArgumentException("bufferedImage cannot be null.");
        }
        BufferedImage bufferedImage = pixelOptions.getSource();
        int blockSize = pixelOptions.getBlockSize();
        IPixelType pixelType = pixelOptions.getPixelType();
        int picWidth = bufferedImage.getWidth();
        int picHeight = bufferedImage.getHeight();

        BufferedImage back = new BufferedImage(picWidth / blockSize * blockSize, picHeight / blockSize * blockSize, bufferedImage.getType());
        Graphics2D g2d = GraphicUtil.getG2d(back);
        g2d.setColor(null);
        g2d.fillRect(0, 0, picWidth, picHeight);
        for (int y = 0; y < picHeight; y += blockSize) {
            for (int x = 0; x < picWidth; x += blockSize) {
                int[] colors = ImgPixelHelper.getPixels(bufferedImage, x, y, blockSize, blockSize);
                Color avgColor = pixelType.getAverage(colors);
                pixelType.draw(g2d, avgColor, x, y, blockSize, blockSize);
            }
        }
        g2d.dispose();
        return back;
    }

    public static class Builder {
        private ImgPixelOptions pixelOptions;

        private Builder() {
            pixelOptions = new ImgPixelOptions();
        }

        public Builder setSourceImg(String img) {
            try {
                return setSourceImg(ImageLoadUtil.getImageByPath(img));
            } catch (Exception e) {
                throw new IllegalArgumentException(img + " error");
            }
        }

        public Builder setSourceImg(BufferedImage img) {
            pixelOptions.setSource(img);
            return this;
        }

        public Builder setBlockSize(int size) {
            pixelOptions.setBlockSize(size);
            return this;
        }

        public Builder setPixelType(IPixelType pixelType) {
            pixelOptions.setPixelType(pixelType);
            return this;
        }

        public ImgPixelWrapper build() {
            if (pixelOptions.getBlockSize() == 0) {
                pixelOptions.setBlockSize(1);
            }

            if (pixelOptions.getPixelType() == null) {
                pixelOptions.setPixelType(PixelStyleEnum.CHAR_COLOR);
            }
            return new ImgPixelWrapper(pixelOptions);
        }
    }

}
