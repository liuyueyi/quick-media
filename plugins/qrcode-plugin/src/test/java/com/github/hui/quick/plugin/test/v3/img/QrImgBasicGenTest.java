package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicTypeEnum;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.Test;

import java.awt.*;

/**
 * @author YiHui
 * @date 2022/9/8
 */
public class QrImgBasicGenTest extends BasicGenTest {
    @Override
    public void init() {
        super.init();
        prefix += "/img";
    }

    /**
     * 最简单的生成方式
     *
     * @throws Exception
     */
    @Test
    public void simpleGen() throws Exception {
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(500)
                .asFile(prefix + "/basic_00.jpg");
        System.out.println(ans);
    }

    /**
     * 异常级别，留白
     *
     * @throws Exception
     */
    @Test
    public void errLevel() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500).setErrorCorrection(ErrorCorrectionLevel.L)
                .setPadding(0).asFile(prefix + "/basic_01.png");
        System.out.println(ans);
    }

    @Test
    public void jpgGen() throws Exception {
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(500)
                // 默认输出的是png格式图片，如果希望输出jpg，请指定PicType
                .setPicType(PicTypeEnum.JPG)
                .asFile(prefix + "/basic_jpg.jpg");
        System.out.println(ans);
    }

    /**
     * 前置色
     *
     * @throws Exception
     */
    @Test
    public void preColor() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500).setPreColor(Color.RED)
                .asFile(prefix + "/basic_pre.png");
        System.out.println(ans);
    }

    /**
     * 设置背景和前置色
     *
     * @throws Exception
     */
    @Test
    public void preBgColor() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500).setPreColor(Color.RED)
                .setBgColor(Color.LIGHT_GRAY).asFile(prefix + "/basic_pre_bg.png");
        System.out.println(ans);
    }

    /**
     * 背景里透明
     *
     * @throws Exception
     */
    @Test
    public void transparent() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500).setBgColor(ColorUtil.OPACITY)
                .asFile(prefix + "/basic_transparent.png");
        System.out.println(ans);
    }

    /**
     * 输出圆角二维码
     *
     * @throws Exception
     */
    @Test
    public void roundQrImg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setPreColor(Color.BLUE)
                // 指定输出圆角二维码
                .setPicStyle(PicStyle.ROUND)
                // 指定圆角的比例
                .setCornerRadius(0.2F)
                .asFile(prefix + "/basic_round.png");
        System.out.println(ans);
    }
}
