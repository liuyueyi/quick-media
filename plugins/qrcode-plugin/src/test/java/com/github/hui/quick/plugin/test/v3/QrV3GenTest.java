package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.TxtMode;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author YiHui
 * @date 2022/8/2
 */
public class QrV3GenTest extends BasicGenTest {
    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "d://quick-media/img";
        }
    }

    @Test
    public void basicGen() {
        try {
            BufferedImage img = QrCodeGenV3.of(msg).build().asImg();
            System.out.println("---");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRender() {
        try {
            String ft = "ft/ft_1.png";
            String bg = "bg.png";
            int size = 1340;
            BufferedImage img = QrCodeGenV3.of(msg)
                    .setW(size)
                    .newDrawOptions()
                    .setBgColor(Color.WHITE)
                    .setPreColor(Color.BLACK)
                    .setDrawStyle(DrawStyle.MINI_RECT)
                    .setTransparencyBgFill(false)
                    .setPicStyle(PicStyle.ROUND).complete()
                    .newLogoOptions()
                    .setLogo(new QrResource().setPicStyle(PicStyle.ROUND).setImg(ImageLoadUtil.getImageByPath("logo.jpg")))
                    .setRate(10).complete()
                    .newBgOptions()
                    .setBg(new QrResource().setImg(ImageLoadUtil.getImageByPath(bg)))
                    .setBgW(size).setBgH(size)
                    .setBgStyle(BgStyle.OVERRIDE)
                    .setOpacity(0.5f).complete()
                    .newFrontOptions()
                    .setStartX(100)
                    .setStartY(130)
                    .setFillColor(Color.WHITE)
                    .setFt(new QrResource().setImg(ImageLoadUtil.getImageByPath(ft))).complete()
                    .build()
                    .asImg();
            System.out.println("over");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 支持指定各种大小的图片资源
     */
    @Test
    public void testImgRender() {
        int size = 500;
        try {
            String bg = "http://img11.hc360.cn/11/busin/109/955/b/11-109955021.jpg";
            BufferedImage img = QrCodeGenV3.of(msg).setW(size)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .newDrawOptions()
                    .setDrawStyle(DrawStyle.IMAGE)
                    .newRenderResource(new QrResource().setImg("jihe/a.png")).build()
                    .createSource(1, 3).addResource(new QrResource().setImg("jihe/b.png")).build()
                    .createSource(3, 1).addResource( new QrResource().setImg("jihe/c.png")).build()
                    .createSource(2, 3).addResource( new QrResource().setImg("jihe/e.png")).build()
                    .createSource(3, 2).addResource( new QrResource().setImg("jihe/f.png")).build()
                    .createSource(2, 2).addResource( new QrResource().setImg("jihe/g.png")).build()
                    .createSource(4, 3).addResource( new QrResource().setImg("jihe/h.png")).build().over()
                    .complete()
                    .newDetectOptions()
                    .setResource(new QrResource().setImg("jihe/PDP.png")).complete()
                    .newBgOptions()
                    .setBgStyle(BgStyle.PENETRATE)
                    .setBg(new QrResource().setImg(bg))
                    .setStartX(10).setStartY(100)
                    .complete()
                    .build().asImg();
            System.out.println("over");
            ImageIO.write(img, "png", new File(prefix + "/q1.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 0点1点都用给的图片进行渲染，输出形如围棋格子的二维码
     */
    @Test
    public void testBgImg() {
        int size = 500;
        try {
            QrCodeGenV3.of(msg).setW(size)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .newDrawOptions()
                    .setDrawStyle(DrawStyle.IMAGE)
                    .setBgColor(ColorUtil.OPACITY)
                    .newRenderResource(new QrResource().setImg("overbg/a.png")).build()// 这个表示1点对应的图
                    .createSource(1, 1)
                    .addResource(new QrResource().setImg("overbg/b.png"), -1).setMiss(0, 0).build() // 这个表示0点对应图
                    .over()
                    .complete()
                    .newDetectOptions().setSpecial(true).complete()
                    .build().asFile(prefix + "/q1.png");
            System.out.println("over");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用文字渲染二维码
     *
     * @throws Exception
     */
    @Test
    public void testTxtImg() throws Exception {
        try {
            int size = 300;
            QrCodeGenV3.of(msg)
                    .setW(size)
                    .setErrorCorrection(ErrorCorrectionLevel.H)
                    .newDrawOptions()
                    .setDrawStyle(DrawStyle.TXT)
                    .setRenderResource(new QrResource().setText("璧月香风万家帘幕烟如昼").setTxtMode(TxtMode.ORDER))
                    .complete()
                    .newDetectOptions().setSpecial(true).complete()
                    .build().asFile(prefix + "/txt.png");
            System.out.println("over");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
