package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
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
//        BufferedImage bf = QrCodeGenWrapper.of(msg)
//                .setDrawBgColor(Color.WHITE)
//                .setDrawPreColor(Color.BLACK)
//                .setDetectInColor(Color.RED)
//                .asBufferedImage();
//        System.out.println("over");

        String logo = "http://img1.imgtn.bdimg.com/it/u=2018939532,1617516463&fm=26&gp=0.jpg";
        String msg = "https://liuyueyi.github.io/hexblog/2018/03/23/mysql之锁与事务详解/";
        int size = 500;
        try {

            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setDrawPreColor(0xff0000ff)
                    .setDrawBgColor(0xffffffff)
                    .setDetectOutColor(0xff00FF00)
                    .setDetectInColor(0xffff0000)
                    .setPadding(1)
                    .setLogo(logo)
                    .setLogoRate(12)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBorder(true)
                    .setLogoBgColor(Color.GREEN)
                    .setBgOpacity(0.8f)
                    .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                    .setBgStartX(108)
                    .setBgStartY(40)
                    .asFile("/tmp/yyy.png");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBase642Ima() throws IOException {
        try {
            BufferedImage bfimg = ImageLoadUtil.getImageByPath("logo.jpg");
            String str = Base64Util.encode(bfimg, "png");
            BufferedImage img = Base64Util.decode2Img(str);
            System.out.println("img");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDrawPng() throws IOException {
        BufferedImage img = ImageLoadUtil.getImageByPath("love/01.png");
        BufferedImage bg = GraphicUtil.createImg(img.getWidth(), img.getHeight(), null);
        Graphics2D g2d = GraphicUtil.getG2d(bg);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        g2d.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
        g2d.dispose();
    }

}
