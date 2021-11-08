package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.FileReadUtil;
import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图片像素化测试类
 *
 * @author yihui
 * @data 2021/11/7
 */
public class ImgPixelHelperTest {

    @Test
    public void testImgGrayAlg() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage source = ImageLoadUtil.getImageByPath(img);
        BufferedImage out = ImgPixelWrapper.toPixelImg(source, 1, PixelStyleEnum.GRAY_ALG);
        System.out.println(out);
    }


    @Test
    public void testImgGrayAvg() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage source = ImageLoadUtil.getImageByPath(img);
        BufferedImage out = ImgPixelWrapper.toPixelImg(source, 1, PixelStyleEnum.GRAY_AVG);
        System.out.println(out);
    }

    @Test
    public void testImgPixel() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage source = ImageLoadUtil.getImageByPath(img);
        BufferedImage out = ImgPixelWrapper.toPixelImg(source, 10, PixelStyleEnum.COLOR_AVG);
        System.out.println(out);
    }

    /**
     * 彩色字符图
     * @throws IOException
     */
    @Test
    public void testCharImg() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage source = ImageLoadUtil.getImageByPath(img);
        BufferedImage out = ImgPixelWrapper.toPixelImg(source, 6, PixelStyleEnum.CHAR_GRAY_ALG);
        System.out.println(out);
    }
}
