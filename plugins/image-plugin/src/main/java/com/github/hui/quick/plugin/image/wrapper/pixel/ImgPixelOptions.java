package com.github.hui.quick.plugin.image.wrapper.pixel;

import com.github.hui.quick.plugin.image.wrapper.pixel.model.IPixelType;

import java.awt.image.BufferedImage;

/**
 * @author yihui
 * @date 21/11/10
 */
public class ImgPixelOptions {

    private BufferedImage source;

    private IPixelType pixelType;

    private int blockSize;

    public BufferedImage getSource() {
        return source;
    }

    public void setSource(BufferedImage source) {
        this.source = source;
    }

    public IPixelType getPixelType() {
        return pixelType;
    }

    public void setPixelType(IPixelType pixelType) {
        this.pixelType = pixelType;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}
