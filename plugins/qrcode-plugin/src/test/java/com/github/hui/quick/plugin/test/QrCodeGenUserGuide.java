package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.ColorUtil;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 二维码插件用户使用实例，教程说明文档参考：
 *  <a href="https://liuyueyi.github.io/quick-media/#/%E6%8F%92%E4%BB%B6/%E4%BA%8C%E7%BB%B4%E7%A0%81/%E4%BA%8C%E7%BB%B4%E7%A0%81%E6%8F%92%E4%BB%B6%E6%A6%82%E8%A7%88">二维码插件概览</a>
 * Created by @author yihui in 21:05 19/11/11.
 */
public class QrCodeGenUserGuide {

    /**
     * 默认的二维码生成
     */
    @Test
    public void defaultQr() {
        try {
            // 生成二维码，并输出为qr.png图片
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg).asFile("/tmp/dq.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void colorQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(300)
                    // 定位点(探测图形)外边颜色
                    .setDetectOutColor(Color.CYAN)
                    // 定位点内部颜色
                    .setDetectInColor(Color.RED)
                    // 二维码着色点
                    .setDrawPreColor(Color.BLUE)
                    // 二维码背景图
                    .setDrawBgColor(0xffffffff)
                    .asFile("/tmp/cqr.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logoQr0() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String logo = "https://static.oschina.net/uploads/user/283/566591_100.jpeg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setLogo(logo)
                    .asFile("/tmp/lqr0.png");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logoQr1() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String logo = "https://static.oschina.net/uploads/user/283/566591_100.jpeg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setLogo(logo)
                    .setLogoBgColor(0xff808080)
                    .setLogoBorder(true)
                    .asFile("/tmp/lqr1.png");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logoQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String logo = "https://static.oschina.net/uploads/user/283/566591_100.jpeg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xffc7c7c7)
                    .setLogoBorder(true)
                    .asFile("/tmp/lqr2.png");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logoQr3() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String logo = "https://static.oschina.net/uploads/user/283/566591_100.jpeg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setLogo(logo)
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xfffefefe)
                    .setLogoBorderBgColor(0xffc7c7c7)
                    .setLogoBorder(true)
                    .asFile("/tmp/lqr3.png");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 背景图的二维码，默认全覆盖模式
     */
    @Test
    public void bgQr1() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8vho8x6r0j20b40b43yl.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bg)
                    .setW(500)
                    .setBgOpacity(0.5f)
                    .asFile("/tmp/bqr1.png");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  指定为填充模式，在背景图的坐标(startX, startY)处绘制二维码(左上角坐标为0,0)
     */
    @Test
    public void bgQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String bg = "http://pic.51yuansu.com/pic3/cover/01/07/09/59015a0e53d83_610.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bg)
                    .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                    .setBgW(500)
                    .setBgH(500)
                    .setBgStartX(130)
                    .setBgStartY(120)
                    .setW(260)
                    .setPadding(0)
                    .setDrawBgColor(0xfff7f7f7)
                    .asFile("/tmp/bqr2.png");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 背景渲染方式，用背景图来填充二维码
     */
    @Test
    public void bgQr3() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String bg = "http://img1.juimg.com/180517/355855-1P51H3520817.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bg)
                    .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                    .setBgW(500)
                    .setBgH(500)
                    .setW(500)
                    .asFile("/tmp/bqr3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 全覆盖背景，并设置二维码的前置渲染图，后置渲染图片
     */
    @Test
    public void bgQr4() {
        String msg = "https://blog.hhui.top";
        String bg = "overbg/bg.jpg";
        String cell = "overbg/a.png";
        String bgCell = "overbg/b.png";

        try {
            Boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bg)
                    .setW(500)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setBgStyle(QrCodeOptions.BgImgStyle.OVERRIDE)
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .setDrawImg(cell)
                    .setDrawBgImg(bgCell)
                    .asFile("/tmp/bqr4.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 探测图形，用指定图片替换
     */
    @Test
    public void detectedQr1() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        try {
            QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setDetectImg("detect.png")
                    .asFile("/tmp/tqr1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 三个探测图形，用三张不同的图片
     */
    @Test
    public void detectedQr2() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        try {
            QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setLTDetectImg("leaf/eye.png")
                    .setLDDetectImg("jihe/PDP.png")
                    .setRTDetectImg("love/01.png")
                    .setDiaphaneityFill(true)
                    .asFile("/tmp/tqr2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void rectQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawStyle(QrCodeOptions.DrawStyle.RECT)
                    .asFile("/tmp/dqr0.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void circleQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                    .asFile("/tmp/dqr1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void triangleQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawStyle(QrCodeOptions.DrawStyle.TRIANGLE)
                    .asFile("/tmp/dqr2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void diamondQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawStyle(QrCodeOptions.DrawStyle.DIAMOND)
                    .asFile("/tmp/dqr3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sexAngleQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawStyle(QrCodeOptions.DrawStyle.SEXANGLE)
                    .asFile("/tmp/dqr4.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void octagonQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawStyle(QrCodeOptions.DrawStyle.OCTAGON)
                    .asFile("/tmp/dqr5.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void circleQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    // 支持合并成一个大的圆点
                    .setDrawEnableScale(true)
                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                    .asFile("/tmp/dqr6.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void diamondQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawEnableScale(true)
                    .setDrawStyle(QrCodeOptions.DrawStyle.DIAMOND)
                    .asFile("/tmp/dqr7.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sexAngleQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawEnableScale(true)
                    .setDrawStyle(QrCodeOptions.DrawStyle.SEXANGLE)
                    .asFile("/tmp/dqr8.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void octagonQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setDrawEnableScale(true)
                    .setDrawStyle(QrCodeOptions.DrawStyle.OCTAGON)
                    .asFile("/tmp/dqr9.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void imgQr1() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

        int size = 500;
        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setDrawBgColor(ColorUtil.OPACITY)
                    .setDetectImg("jihe/PDP.png")
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .addImg(1, 1, "jihe/a.png")
                    .addImg(3, 1, "jihe/b.png")
                    .addImg(1, 3, "jihe/c.png")
                    .addImg(3, 2, "jihe/e.png")
                    .addImg(2, 3, "jihe/f.png")
                    .addImg(2, 2, "jihe/g.png")
                    .addImg(3, 4, "jihe/h.png")
                    .setPicType("png")
                    .asFile("/tmp/imgQr1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void imgQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            int size = 500;
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
                    .setErrorCorrection(ErrorCorrectionLevel.M)
                    .setDrawBgColor(ColorUtil.OPACITY)
                    .setDetectImg("love/01.png")
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .addImg(1, 1, "love/003_01.png")
                    .addImg(4, 1, "love/004.png")
                    .addImg(1, 4, "love/004_02.png")
                    .asFile("/tmp/imgQr2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void imgQr3() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            int size = 500;
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(size)
                    .setH(size)
                    .setErrorCorrection(ErrorCorrectionLevel.M)
                    .setDrawBgColor(ColorUtil.OPACITY)
                    .setDrawBgImg("overbg/b.png")
                    .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE)
                    .setDrawImg("overbg/a.png")
                    .asFile("/tmp/imgQr3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态二维码，和背景图的使用姿势一样，唯一的区别就是背景图是动态的
     */
    @Test
    public void gifQr1() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8pq78mcgrg20dw0boaja.gif";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setBgImg(bg)
                    .setBgOpacity(0.6f)
                    .setPicType("gif")
                    .asFile("/tmp/gifQr1.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void gifQr2() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8qe2iv0evg20xc0irn68.gif";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setBgImg(bg)
                    .setBgStyle(QrCodeOptions.BgImgStyle.FILL)
                    .setBgStartX(20)
                    .setBgStartY(137)
                    .setPicType("gif")
                    .asFile("/tmp/gifQr2.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void gifQr3() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8w7wj6qvsg20oy0io4dt.gif";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bg)
                    .setBgW(500)
                    .setBgH(500)
                    .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                    .setW(500)
                    .asFile("/tmp/gifQr3.gif");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
