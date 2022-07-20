package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;

/**
 * 动态二维码生成测试
 * Created by @author yihui in 19:00 19/11/7.
 */
public class AnimateQrCodeTest {

    @Test
    public void testGenAnimateColorCode() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
        try {
            String logo = "logo.jpg";
            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8pq78mcgrg20dw0boaja.gif";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setDrawPreColor(0xff002fa7) // 宝石蓝
                    .setDetectOutColor(0xff00ff00)
                    .setDetectInColor(0xffff0000)
                    .setPadding(1)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setLogo(logo)
                    .setLogoBorder(true)
                    .setLogoBgColor(Color.LIGHT_GRAY)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setBgImg(bg)
                    .setBgOpacity(0.6f)
                    .setPicType("gif")
                    .asFile("/tmp/out.gif");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("cmvreate qrcode error! e: " + e);
        }
    }


    @Test
    public void testGenAnimateColorCodeV2() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        // 根据本地文件生成待logo的二维码， 重新着色位置探测图像
        try {
            String logo = "logo.jpg";
            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8qe2iv0evg20xc0irn68.gif";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .setDetectImg("jihe/PDP.png")
                    .addImg(1, 1, "jihe/a.png")
                    .addImg(3, 1, "jihe/b.png")
                    .addImg(1, 3, "jihe/c.png")
                    .addImg(3, 2, "jihe/e.png")
                    .addImg(2, 3, "jihe/f.png")
                    .addImg(2, 2, "jihe/g.png")
                    .addImg(3, 4, "jihe/h.png")
                    .setPadding(1)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setLogo(logo)
                    .setLogoBorder(true)
                    .setBgImg(bg)
                    .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                    .setBgStartX(20)
                    .setBgStartY(137)
                    .setBgOpacity(0.9f)
                    .setPicType("gif")
                    .asFile("/tmp/out2.gif");
            System.out.println(ans);
        } catch (Exception e) {
            System.out.println("cmvreate qrcode error! e: " + e);
        }
    }

    @Test
    public void gifQr4() throws IOException {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8w9jsxwtdg20pz08zwr8.gif";
        String logo = "logo.jpg";
        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setDrawBgColor(ColorUtil.OPACITY)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .setDetectImg("jihe/PDP.png")
                    .addImg(1, 1, "jihe/a.png")
                    .addImg(3, 1, "jihe/b.png")
                    .addImg(1, 3, "jihe/c.png")
                    .addImg(3, 2, "jihe/e.png")
                    .addImg(2, 3, "jihe/f.png")
                    .addImg(2, 2, "jihe/g.png")
                    .addImg(3, 4, "jihe/h.png")
                    .setPadding(1)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setLogo(logo)
                    .setLogoBorder(true)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xfffefefe)
                    .setLogoBorderBgColor(0xffc7c7c7)
                    .setBgImg(bg)
                    .setBgW(1870)
                    .setBgH(646)
                    .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                    .setBgStartX(690)
                    .setBgStartY(20)
                    .setBgOpacity(0.9f)
                    .setPicType("gif")
                    .asFile("/tmp/gifQr4.gif");
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
