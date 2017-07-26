package com.hust.hui.quickmedia.common.test;

import com.hust.hui.quickmedia.common.qrcode.QrCodeOptions;
import com.hust.hui.quickmedia.common.util.ImageUtil;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yihui on 2017/7/26.
 */
public class GraphicTest {


    @Test
    public void testDrawRect() throws IOException {
        BufferedImage bufferedImage = ImageUtil.getImageByPath("logo.jpg");

        int size = bufferedImage.getWidth() / 15;
        BufferedImage bf2 = ImageUtil.makeRoundBorder(bufferedImage, QrCodeOptions.LogoStyle.ROUND, size, Color.RED);



        int radius = 40;
        BufferedImage bf1 = ImageUtil.makeRoundedCorner(bufferedImage, radius);
        System.out.println("----");


    }

}
