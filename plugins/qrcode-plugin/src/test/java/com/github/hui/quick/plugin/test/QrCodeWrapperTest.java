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
import junit.framework.Assert;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by yihui on 2017/7/17.
 */
@Slf4j
public class QrCodeWrapperTest {

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
            Assert.assertTrue(false);
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
            Assert.assertTrue(false);
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
                    .setLogoBgColor(0xff808080)
                    .setLogoBorder(true)
                    .asFile("src/test/qrcode/gen_300x300_logo.png");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
            Assert.assertTrue(false);
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
            Assert.assertTrue(false);
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
            Assert.assertTrue(false);
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
            Assert.assertTrue(false);
        }
    }


    @Test
    public void testGenColorCode() {
        String msg = "https://liuyueyi.github.io/hexblog/";
        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
        try {
            String logo = "mg.jpg";
            String bg = "bg.png";
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setDrawPreColor(0xff002fa7) // 宝石蓝
                    .setDetectOutColor(0xff00ff00)
                    .setDetectInColor(0xffff0000)
                    .setPadding(1)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xff00cc00)
                    .setBgImg(bg)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE.name())
                    .setDrawImg("xhrSize4.jpg")
                    .asBufferedImage();


            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            String img64 = Base64Util.encode(outputStream);
            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (Exception e) {
            System.out.println("cmvreate qrcode error! e: " + e);
            Assert.assertTrue(false);
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
                        .setDrawImg("xhrBase.jpg")
                        .setDrawEnableScale(true)
                        .asBufferedImage();


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(img, "png", outputStream);
                String img64 = Base64Util.encode(outputStream);
                System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
            } catch (Exception e) {
                System.out.println("create qrcode error! e: " + e);
                Assert.assertTrue(false);
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
                    .setDrawImg("xhrBase.jpg")
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
            Assert.assertTrue(false);
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

    @Test
    public void testGenLoveQrCode() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

        int size = 500;
        try {
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
                    .setDetectImg("jihe/PDP.png")
                    .setErrorCorrection(ErrorCorrectionLevel.M)
                    .setDrawEnableScale(true)
                    .setDrawStyle(QrCodeOptions.DrawStyle.RECT)
                    .addImg(1, 1, "love/003_02.png")
                    .addImg(1, 4, "love/004.png")
                    .addImg(4, 1, "love/004_02.png")
                    .asBufferedImage();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img, "png", outputStream);
            String img64 = Base64Util.encode(outputStream);
            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenJiheQrCode() {
//        String msg = "http://spring.hhui.top/spring-blog/2019/03/10/190310-SpringCloud%E5%9F%BA%E7%A1%80%E7%AF%87AOP%E4%B9%8B%E6%8B%A6%E6%88%AA%E4%BC%98%E5%85%88%E7%BA%A7%E8%AF%A6%E8%A7%A3/";
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

        int size = 500;
        try {
//            String bg = "http://bpic.588ku.com/master_pic/00/10/04/8656612a16e2e6a.jpg";
            String bg = "http://img11.hc360.cn/11/busin/109/955/b/11-109955021.jpg";

            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
                    .setDetectImg("jihe/PDP.png")
//                    .setDetectImg("love/01.png")
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setDrawEnableScale(true)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .addImg(1, 1, "jihe/a.png")
                    .addImg(3, 1, "jihe/b.png")
                    .addImg(1, 3, "jihe/c.png")
                    .addImg(3, 2, "jihe/e.png")
                    .addImg(2, 3, "jihe/f.png")
                    .addImg(2, 2, "jihe/g.png")
                    .addImg(3, 4, "jihe/h.png")
                    .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                    .setBgOpacity(1.0f)
                    .setBgImg(bg)
                    .setBgStartX(10)
                    .setBgStartY(100)
                    .asBufferedImage();

         ImageIO.write(img, "png", new File("/tmp/q1.png"));

//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ImageIO.write(img, "png", outputStream);
//            String img64 = Base64Util.encode(outputStream);
//            System.out.println("<img src=\"data:image/png;base64," + img64 + "\" />");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
