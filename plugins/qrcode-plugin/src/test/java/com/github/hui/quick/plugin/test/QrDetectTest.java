package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.google.zxing.WriterException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 二维码探测图形颜色指定测试
 *
 * Created by yihui on 2018/3/30.
 */
public class QrDetectTest {

    private String msg = "https://liuyueyi.github.io/hexblog/";

    @Test
    public void testGenQr() throws IOException, WriterException {
        BufferedImage bf = QrCodeGenWrapper.of(msg)
                .setDrawBgColor(Color.WHITE)
                .setDrawPreColor(Color.BLACK)
                .setDetectInColor(Color.RED)
                .asBufferedImage();
        System.out.println("over");
    }

    @Test
    public void testBase642Ima() throws IOException {
        BufferedImage bfimg = ImageIO.read(new File("/Users/yihui/22.png"));
        String str = Base64Util.encode(bfimg, "png");

        try {
            BufferedImage img = Base64Util.decode2Img(str);
            System.out.println("img");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
