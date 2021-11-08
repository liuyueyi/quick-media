package com.github.hui.quick.plugin.image.wrapper.pixel;

import com.github.hui.quick.plugin.base.GraphicUtil;
import com.github.hui.quick.plugin.image.helper.ImgPixelHelper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.IPixelType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author yihui
 * @date 21/11/8
 */
public class ImgPixelWrapper {

    static {
        ImageIO.scanForPlugins();
    }

    /**
     * 转成像素图片
     *
     * @param bufferedImage 原图片
     * @param blockSize     块大小
     * @return
     */
    public static BufferedImage toPixelImg(BufferedImage bufferedImage, int blockSize, IPixelType pixelType) {
        if (bufferedImage == null) {
            throw new IllegalArgumentException("bufferedImage cannot be null.");
        }
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

}
