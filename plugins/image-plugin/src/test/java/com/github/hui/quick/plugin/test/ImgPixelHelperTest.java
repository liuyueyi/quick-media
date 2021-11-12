package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.image.wrapper.pixel.ImgPixelWrapper;
import com.github.hui.quick.plugin.image.wrapper.pixel.model.PixelStyleEnum;
import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 图片像素化测试类
 *
 * @author yihui
 * @data 2021/11/7
 */
public class ImgPixelHelperTest {
    private String prefix = "/tmp";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "c://quick-media";
        }
    }

    @Test
    public void testImgGrayAlg() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage out = ImgPixelWrapper.build().setSourceImg(img).setBlockSize(1).setPixelType(PixelStyleEnum.GRAY_ALG).build().asBufferedImg();
        System.out.println(out);
    }


    @Test
    public void testImgGrayAvg() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage out = ImgPixelWrapper.build().setSourceImg(img).setBlockSize(1).setPixelType(PixelStyleEnum.GRAY_AVG).build().asBufferedImg();
        System.out.println(out);
    }

    @Test
    public void testImgPixel() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage out = ImgPixelWrapper.build().setSourceImg(img).setBlockSize(1).setPixelType(PixelStyleEnum.PIXEL_COLOR_AVG).build().asBufferedImg();
        System.out.println(out);
    }

    /**
     * 彩色字符图
     * @throws IOException
     */
    @Test
    public void testCharImg() throws IOException {
//        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        String img = "http://hbimg.b0.upaiyun.com/2b79e7e15883d8f8bbae0b1d1efd6cf2c0c1ed1b10753-cusHEA_fw236";
        BufferedImage out = ImgPixelWrapper.build().setSourceImg(img).setBlockSize(2)
                .setPixelType(PixelStyleEnum.CHAR_COLOR)
                .setRate(1.5)
                .setChars("灰")
                .build()
                .asBufferedImg();
        System.out.println(out);
    }

    @Test
    public void testGif() throws Exception {
        String img = "https://img.zcool.cn/community/01565859a4ea21a801211d251e1cbc.gif";
        ImgPixelWrapper.build().setSourceImg(img)
                .setBlockSize(6)
                .setPixelType(PixelStyleEnum.CHAR_COLOR)
                .setRate(1.5)
                .setChars("灰")
                .build()
                .asFile(prefix + "/hr.gif");
        System.out.println("--------");
    }
}
