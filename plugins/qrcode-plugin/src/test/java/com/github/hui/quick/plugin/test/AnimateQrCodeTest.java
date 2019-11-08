package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

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
            String logo = "lobo_logo.png";
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
                    .setBgImg(bg)
                    .setBgOpacity(0.9f)
                    .setPicType("jpg")
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
}
