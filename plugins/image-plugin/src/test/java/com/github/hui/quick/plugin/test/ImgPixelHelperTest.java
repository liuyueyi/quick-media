package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.ImageLoadUtil;
import com.github.hui.quick.plugin.image.helper.ImgPixelHelper;
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

    @Test
    public void testImgPixel() throws IOException {
        String img = "https://c-ssl.duitang.com/uploads/item/201809/16/20180916175034_Gr2hk.thumb.1000_0.jpeg";
        BufferedImage source = ImageLoadUtil.getImageByPath(img);
        BufferedImage out = ImgPixelHelper.getBlockBitmap(source, 10);
        System.out.println(out);
    }
}
