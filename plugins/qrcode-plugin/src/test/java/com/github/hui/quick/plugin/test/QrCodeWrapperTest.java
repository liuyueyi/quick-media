package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.Base64Util;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeDeWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by yihui on 2017/7/17.
 */
public class QrCodeWrapperTest {
    private static final Logger log = LoggerFactory.getLogger(QrCodeWrapperTest.class);

    private String msg = "https://liuyueyi.github.io/hexblog/2018/03/23/mysql之锁与事务详解/";


    @Test
    public void testDepaseQrCode() throws FormatException, ChecksumException, NotFoundException, IOException {
        String img = "https://static.oschina.net/uploads/img/201707/09205944_0PzS.jpg";

        String ans = QrCodeDeWrapper.decode(img);
        System.out.println(ans);
    }


    /**
     * 测试二维码
     */
    @Test
    public void testGenQrCode() {
        try {

            boolean ans = QrCodeGenWrapper.of(msg).asFile("src/test/qrcode/gen.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
        }


        //生成红色的二维码 300x300, 无边框
        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setDrawPreColor(0xffff0000)
                    .setDrawBgColor(0xffffffff)
                    .setPadding(0)
                    .asFile("src/test/qrcode/gen_300x300.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
        }


        // 生成带logo的二维码
        try {
            String logo = "https://static.oschina.net/uploads/user/283/566591_100.jpeg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setDrawPreColor(0xffff0000)
                    .setDrawBgColor(0xffffffff)
                    .setPadding(0)
                    .setLogo(logo)
//                    当不指定logo的样式和边框时，如果logo为png，那么透明的地方会显示二维码信息
                    .setLogoBgColor(0xff808080)
                    .setLogoBorder(true)
                    .asFile("src/test/qrcode/gen_300x300_logo.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
        }


        // 根据本地文件生成待logo的二维码
        try {
            String logo = "logo.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setDrawPreColor(0xffff0000)
                    .setDrawBgColor(0xffffffff)
                    .setPadding(0)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xff00ff00)
                    .setLogoBorder(true)
                    .asFile("src/test/qrcode/gen_300x300_logo_v2.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
        }


        // 根据本地文件生成待logo的二维码，无边框， 重新着色位置探测图像， 设置背景
        try {
            String logo = "logo.jpg";
            String bg = "bg.png";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    .setDrawPreColor(0xff0000ff)
                    .setDrawBgColor(0xffffffff)
                    .setDetectOutColor(0xff00FF00)
                    .setDetectInColor(0xffff0000)
                    .setPadding(1)
                    .setLogo(logo)
                    .setLogoRate(15)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setBgImg(bg)
                    .setBgOpacity(0.8f)
                    .asFile("src/test/qrcode/gen_300x300_logo_v3.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            
        }


        // 根据本地文件生成待logo的二维码，无边框， 重新着色位置探测图像， 设置背景
        try {
            String logo = "logo.jpg";
            String bg = "bg.png";
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
                    .setBgImg(bg)
                    .setBgOpacity(0.8f)
                    .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                    .setBgStartX(108)
                    .setBgStartY(40)
                    .asFile("src/test/qrcode/gen_300x300_logo_v4.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            
        }
    }


    @Test
    public void testGenColorCode() {
        String msg = "https://liuyueyi.github.io/hexblog/";
        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
        try {
            String logo = "lobo_logo.png";
            String bg = "bg.png";
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setDrawPreColor(0xff002fa7) // 宝石蓝
                    .setDetectOutColor(0xff00ff00)
                    .setDetectInColor(0xffff0000)
                    .setPadding(1)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setLogo(logo)
                    .setLogoBorder(true)
//                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
//                    .setLogoBgColor(0xff000000)
                    .setBgImg(bg)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE.name())
                    .setDrawImg("xhr/xhrSize4.jpg")
                    .setPicType("jpg")
                    .asBufferedImage();


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", outputStream);
            String img64 = Base64Util.encode(outputStream);
            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (Exception e) {
            System.out.println("cmvreate qrcode error! e: " + e);
        }
    }


    @Test
    public void testGenStyleCode() {
        String msg = "https://liuyueyi.github.io/hexblog/2018/03/23/mysql之锁与事务详解/";
//        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像

        for (QrCodeOptions.DrawStyle style : QrCodeOptions.DrawStyle.values()) {

            try {
                String logo = "mg.jpg";
                String bg = "bg.png";
                BufferedImage img = QrCodeGenWrapper.of(msg)
                        .setW(540)
                        .setDrawPreColor(0xff002fa7) // 宝石蓝
                        .setDetectOutColor(0xff0000ff)
                        .setDetectInColor(Color.RED)
                        .setPadding(1)
                        .setErrorCorrection(ErrorCorrectionLevel.H)
                        .setLogo(logo)
                        .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                        .setLogoBgColor(0xff00cc00)
                        .setLogoRate(15)
                        .setBgImg(bg)
                        .setBgOpacity(0.93f)
                        .setDrawStyle(style.name())
                        .setDrawImg("xhr/xhrBase.jpg")
                        .setDrawEnableScale(true)
                        .asBufferedImage();


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(img, "png", outputStream);
                String img64 = Base64Util.encode(outputStream);
                System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
            } catch (Exception e) {
                System.out.println("create qrcode error! e: " + e);
                
            }
        }
    }


    @Test
    public void testGenStyleCodeV2() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
//        String msg = "https://my.oschina.net/u/566591/blog";

        try {
            String logo = "logo.jpg";
//            String bg = "qrbg.jpg";
            String bg = "http://bpic.588ku.com/master_pic/00/10/04/8656612a16e2e6a.jpg";
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(550)
                    .setDrawPreColor(0xff002fa7) // 宝石蓝
                    .setDetectOutColor(0xff0000ff)
                    .setDetectInColor(Color.RED)
                    .setDetectImg("detect.png")
                    .setPadding(1)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xff00cc00)
                    .setLogoRate(15)
                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE.name())
                    .setDrawEnableScale(true)
                    .setDrawImg("xhr/xhrBase.jpg")
                    .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                    .setBgOpacity(1.0f)
                    .setBgImg(bg)
                    .setBgStartX(10)
                    .setBgStartY(100)
                    .asBufferedImage();


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            String img64 = Base64Util.encode(outputStream);
            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            
        }
    }


    @Test
    public void testGenWxQrcode() {
        String logo = "logo.jpg";

        int size = 500;
        try {
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
//                    .setDetectImg("detect.png")
                    .setDrawPreColor(0xff008e59)
//                    .setDrawPreColor(0xff002fa7)
                    .setErrorCorrection(ErrorCorrectionLevel.M)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(Color.LIGHT_GRAY)
                    .setLogo(logo)
                    .setLogoRate(10)
                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                    .setDrawEnableScale(true)
                    .asBufferedImage();
            ImageIO.write(img, "png", new File("src/test/qrcode/style.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
