package com.github.hui.quick.plugin.test;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.image.wrapper.create.ImgCreateOptions;
import com.github.hui.quick.plugin.image.wrapper.merge.ImgMergeWrapper;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.ImgCell;
import com.github.hui.quick.plugin.image.wrapper.merge.cell.TextCell;
import com.github.hui.quick.plugin.qrcode.entity.RenderImgResourcesV2;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeOptions;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * 二维码插件用户使用实例，教程说明文档参考：
 * <a href="https://liuyueyi.github.io/quick-media/#/%E6%8F%92%E4%BB%B6/%E4%BA%8C%E7%BB%B4%E7%A0%81/%E4%BA%8C%E7%BB%B4%E7%A0%81%E6%8F%92%E4%BB%B6%E6%A6%82%E8%A7%88">二维码插件概览</a>
 * Created by @author yihui in 21:05 19/11/11.
 */
public class QrCodeGenUserGuide {

    private String prefix = "/tmp";

    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "c://quick-media";
        }
    }

    /**
     * 默认的二维码生成
     */
    @Test
    public void defaultQr() {
        try {
            // 生成二维码，并输出为qr.png图片
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg).asFile(prefix + "/dq.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认的二维码生成
     */
    @Test
    public void defaultQr_corner() {
        try {
            // 生成二维码，并输出为qr.png图片
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setPadding(3)
                    .setQrStyle(QrCodeOptions.ImgStyle.ROUND)
                    .setQrCornerRadiusRate(0.125F)
                    .setPicType("png")
                    .asFile(prefix + "/dq_corner1.png");
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
                    // 探测图形特殊处理
                    .setDetectSpecial()
                    // 二维码背景图
                    .setDrawBgColor(0xffffffff)
                    .asFile(prefix + "/cqr.png");
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
                    .asFile(prefix + "/lqr0.png");
        } catch (Exception e) {
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
                    .asFile(prefix + "/lqr1.png");
        } catch (Exception e) {
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
                    .asFile(prefix + "/lqr2.png");
        } catch (Exception e) {
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
                    .asFile(prefix + "/lqr3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 带外边框的圆形logo
     */
    @Test
    public void logoQr4() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String logo = "logo.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setLogo(logo)
                    // 圆形logo支持
                    .setLogoStyle(QrCodeOptions.LogoStyle.CIRCLE)
                    .setLogoBgColor(0xfffefefe)
                    .setLogoBorderBgColor(0xffc7c7c7)
                    .setLogoBorder(true)
                    .asFile(prefix + "/lqr4.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 非矩形的logo
     */
    @Test
    public void logoQr5() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String logo = "logo/rec_logo.jpg";
            BufferedImage img = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setLogo(logo)
                    // 圆形logo支持
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xfffefefe)
                    .setLogoBorderBgColor(0xffc7c7c7)
                    .setLogoBorder(true)
                    .asBufferedImage();
//                    .asFile(prefix + "/lqr5.png");
            System.out.println(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 带外边框的圆形logo
     */
    @Test
    public void logoQr6() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String logo = "logo.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(400)
                    .setLogo(logo)
                    // 圆形logo支持
                    .setLogoStyle(QrCodeOptions.LogoStyle.ROUND)
                    .setLogoBgColor(0xfffefefe)
                    .setLogoBorderBgColor(0xffc7c7c7)
                    .setLogoBorder(true)
                    .asFile(prefix + "/lqr6.png");
        } catch (Exception e) {
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
                    .asFile(prefix + "/bqr1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bgQr1_corner() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String bg = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2859744156,2204003006&fm=26&gp=0.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bg)
                    .setW(500)
                    .setBgOpacity(0.6f)
                    // 背景圆角比例
                    .setBgCornerRadiusRate(0.125f)
                    .setBgImgStyle(QrCodeOptions.ImgStyle.ROUND)
                    .asFile(prefix + "/bqr1_c1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bgQr1_circle() {
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
                    .setBgImgStyle(QrCodeOptions.ImgStyle.CIRCLE)
                    .asFile(prefix + "/bqr2_c2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 指定为填充模式，在背景图的坐标(startX, startY)处绘制二维码(左上角坐标为0,0)
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
                    .asFile(prefix + "/bqr2.png");
        } catch (Exception e) {
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
//            String bg = "http://img1.juimg.com/180517/355855-1P51H3520817.jpg";
            String bg = "d://bg.jpg";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bg)
                    .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                    .setBgW(500)
                    .setBgH(500)
                    .setW(500)
                    .asFile("d://bqr3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void bgQrTxt() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            BufferedImage bgImg = GraphicUtil.createImg(500, 500, null);
            Graphics2D g2d = GraphicUtil.getG2d(bgImg);
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, 500, 500);

            Font font = new Font("宋体", Font.BOLD, 250);
            g2d.setFont(font);
            g2d.setColor(Color.RED);
            g2d.drawString("码", 0, 500 - g2d.getFontMetrics().getDescent() / 2);
            g2d.dispose();

            boolean ans = QrCodeGenWrapper.of(msg)
                    .setBgImg(bgImg)
                    .setBgStyle(QrCodeOptions.BgImgStyle.PENETRATE)
                    .setBgW(500)
                    .setBgH(500)
                    .setW(500)
                    .asFile(prefix + "/bqrTxt.png");
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
                    .setDetectSpecial()
                    .asFile(prefix + "/bqr4.png");
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
                    .asFile(prefix + "/tqr1.png");
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
                    .asFile(prefix + "/tqr2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void detectedQr3() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        try {
            QrCodeGenWrapper.of(msg)
                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                    .setDrawEnableScale(true)
                    .asFile(prefix + "/tqr3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void detectedQr4() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        try {
            QrCodeGenWrapper.of(msg)
                    .setDetectSpecial()
                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                    .setDrawEnableScale(true)
                    .asFile(prefix + "/tqr4.png");
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
                    .asFile(prefix + "/dqr0.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void miniRectQr() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(200)
//                    .setDetectSpecial()
                    .setDrawStyle(QrCodeOptions.DrawStyle.MINI_RECT)
                    .asFile(prefix + "/dqr0_1.png");
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
                    .asFile(prefix + "/dqr1.png");
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
                    .asFile(prefix + "/dqr2.png");
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
                    .asFile(prefix + "/dqr3.png");
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
                    .asFile(prefix + "/dqr4.png");
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
                    .asFile(prefix + "/dqr5.png");
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
                    .asFile(prefix + "/dqr6.png");
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
                    .asFile(prefix + "/dqr7.png");
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
                    .asFile(prefix + "/dqr8.png");
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
                    .asFile(prefix + "/dqr9.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fontQr0() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setDrawStyle(QrCodeOptions.DrawStyle.TXT)
                    .setDrawEnableScale(true)
                    .setPicType("png")
                    .asFile(prefix + "/fontQr0.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文字二维码，顺序方式渲染
     */
    @Test
    public void fontQr1() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setDrawStyle(QrCodeOptions.DrawStyle.TXT)
                    .setPicType("png")
                    .asFile(prefix + "/fontQr1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文字二维码，顺序方式渲染
     */
    @Test
    public void fontQr2() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    // 不输入文字时，默认采用千字文
                    // 默认文字顺序渲染
                    // true 则探测图形有自己的绘制规则
                    .setDetectSpecial()
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setDrawStyle(QrCodeOptions.DrawStyle.TXT)
                    .setPicType("png")
                    .asFile(prefix + "/fontQr2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文字二维码
     */
    @Test
    public void fontQr3() {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";

        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setQrText("欢迎关注一灰灰")
                    // 指定文字随机渲染方式
                    .setQrTxtMode(QrCodeOptions.TxtMode.RANDOM)
                    // true 则探测图形有自己的绘制规则
                    .setDetectSpecial()
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .setDrawStyle(QrCodeOptions.DrawStyle.TXT)
                    // 当相邻的NxN都是黑色小方块时，放大（慎用，因为部分汉子如 `一` 无法友好的填充2x2的方块）
                    .setDrawEnableScale(true)
                    .setPicType("png")
                    .asFile(prefix + "/fontQr3.png");
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
                    .asFile(prefix + "/imgQr1.png");
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
                    .addImg(1, 1, "love/001.png")
                    .addImg(2, 2, "love/003_01.png")
                    .addImg(2, 2, "love/003_02.png")
                    .addImg(2, 2, "love/003_03.png")
                    .addImg(4, 1, "love/004.png")
                    .addImg(1, 4, "love/004_02.png")
                    .asFile(prefix + "/imgQr2.png");
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
                    .asFile(prefix + "/imgQr3.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void imgQr3_v2() throws IOException, WriterException {
        String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
        int size = 500;
        boolean ans = QrCodeGenWrapper.of(msg)
                .setW(size)
                .setH(size)
                .setErrorCorrection(ErrorCorrectionLevel.M)
                .setDrawBgColor(ColorUtil.OPACITY)
                .setImgResourcesForV2(RenderImgResourcesV2.create()
                        .addSource(1, 1).addImg("overbg/b.png", -1).setMiss(0, 0).build()
                        .addSource(1, 1).addImg("overbg/a.png", -1).build()
                )
                .setDrawStyle(QrCodeOptions.DrawStyle.IMAGE_V2)
                .asFile(prefix + "/imgQr3_v2.png");
    }

    @Test
    public void imgQr4() {
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
                    .setDetectSpecial()
                    .asFile(prefix + "/imgQr4.png");
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
                    .asFile(prefix + "/gifQr1.gif");
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
                    .asFile(prefix + "/gifQr2.gif");
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
                    .asFile(prefix + "/gifQr3.gif");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 前置图测试
     */
    @Test
    public void ft1() {
        try {
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            String ft = "ft/ft_1.png";
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setDetectImg("eye3.png")
                    .setFtImg(ft)
                    .setFtFillColor(Color.WHITE)
                    .setW(1340)
                    .setH(1340)
                    .setFtStartX(100)
                    .setFtStartY(130)
                    .asFile(prefix + "/ft1.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在二维码周边添加文字的示例
     */
    @Test
    public void ft2() {
        //  创建一个文字的前置图
        BufferedImage ftImg = GraphicUtil.createImg(500, 600, null);
        Graphics2D g2d = GraphicUtil.getG2d(ftImg);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 500, 500, 500);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font(null, Font.PLAIN, 22));
        g2d.drawString("欢迎关注《一灰灰Blog》", 140, 550);


        String msg = "http://weixin.qq.com/r/FS9waAPEg178QrUcL93oH";
        try {
            boolean ans = QrCodeGenWrapper.of(msg)
                    .setW(500)
                    .setH(500)
                    .setDetectImg("eye3.png")
                    .setFtImg(ftImg)
                    .setFtFillColor(Color.WHITE)
                    .asFile(prefix + "/ft2.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在二维码周边添加文字的实例，借助 image-plugins 来实现，能更简单的满足UI大佬的设计需求
     */
    @Test
    public void ft3() {
        try {
            // 二维码
            int qrSize = 460;
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            BufferedImage qrImg = QrCodeGenWrapper.of(msg)
                    .setW(qrSize)
                    .setH(qrSize)
                    .setDetectImg("eye3.png")
                    .setFtFillColor(Color.WHITE)
                    .asBufferedImage();
            ImgCell qrCell = ImgCell.builder().img(qrImg).x(20).y(20).build();


            // 修饰文字
            TextCell textCell = TextCell.builder().font(new Font("宋体", Font.BOLD, 22))
                    .color(Color.BLACK)
                    .drawStyle(ImgCreateOptions.DrawStyle.VERTICAL_LEFT)
                    .alignStyle(ImgCreateOptions.AlignStyle.CENTER)
                    .startX(540)
                    .startY(0)
                    .endX(540)
                    .endY(500)
                    .text("欢迎关注<一灰灰Blog>")
                    .build();

            BufferedImage out = ImgMergeWrapper.merge(Arrays.asList(qrCell, textCell), 600, 500, Color.WHITE);
            ImageIO.write(out, "jpg", new File(prefix + "/ft3.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void preGif() {
//        String pre = "http://1832.img.pp.sohu.com.cn/images/blog/2009/10/23/20/24/12530644e76g215.jpg";
//        String pre = "http://1812.img.pp.sohu.com.cn/images/blog/2009/10/23/20/11/12530710098g214.jpg";
        String pre = "https://iknow-pic.cdn.bcebos.com/f9dcd100baa1cd11dec1529bbd12c8fcc3ce2d11";
        try {
            // 二维码
            int qrSize = 320;
            String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";
            QrCodeGenWrapper.of(msg)
                    .setW(qrSize)
                    .setH(qrSize)
                    .setDrawPreColor(0xff73a7f5)
//                    .setDrawStyle(QrCodeOptions.DrawStyle.CIRCLE)
                    .setDrawEnableScale(true)
                    .setFtImg(pre)
                    .setFtStartX(0)
                    .setFtStartY(-25)
                    .setFtFillColor(Color.WHITE)
                    .asFile(prefix + "/ft_1.gif");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
