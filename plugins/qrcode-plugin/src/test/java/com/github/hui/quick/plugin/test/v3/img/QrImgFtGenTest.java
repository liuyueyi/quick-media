package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.base.awt.GraphicUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 前置图使用实例
 * - 从上到下的层级来看，应该是：
 * - 前置图 -> logo -》 二维码 -> 背景图
 *
 * @author YiHui
 * @date 2022/9/14
 */
public class QrImgFtGenTest extends BasicGenTest {

    @Override
    public void init() {
        super.init();
        this.prefix += "/img";
    }

    /**
     * 前置图基本使用姿势，给二维码加个装饰
     *
     * @throws Exception
     */
    @Test
    public void basicFt() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(1340)
                .setFtResource(new QrResource().setImg("ft/ft_1.png"))
                .setFtX(100)
                .setFtY(130)
                .setFtFillColor(Color.WHITE)
                .asFile(prefix + "/ft_basic.png");
        System.out.println(ans);
    }

    /**
     * 将前置图放在右下角，与其他三个码眼保证相似的特性
     *
     * @throws Exception
     */
    @Test
    public void rightCornerFt() throws Exception {
        int size = 500;
        int ftSize = 500 / 5;
        boolean ans = QrCodeGenV3.of(msg).setSize(size)
                .newFrontOptions()
                .setFt(new QrResource().setImg("cross.png"))
                .setFtW(ftSize)
                .setFtH(ftSize)
                .setStartX((int) (ftSize * 1.2 - size))
                .setStartY((int) (ftSize * 1.2 - size))
                .complete()
                .asFile(prefix + "/ft_right_bottom.png");
        System.out.println(ans);
    }


    /**
     * 前置图实现logo的case
     *
     * @throws Exception
     */
    @Test
    public void logoFt() throws Exception {
        int size = 500;
        int ftSize = size / 5;
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(size)
                .clearLogoArea(true)
                .setFtResource(new QrResource().setImg("cross.png"))
                .setFtW(ftSize).setFtH(ftSize)
                .setFtX((ftSize - size) / 2)
                .setFtY((ftSize - size) / 2)
                .asFile(prefix + "/ft_logo.png");
        System.out.println(ans);
    }

    @Test
    public void geoFt() throws Exception {
        int size = 500;
        int ftSize = size / 5;
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(size)
                .clearLogoArea(true)
                // 使用预设、自定义的绘制样式时，必须显示设置 ftW, ftH, ftStartX, ftStartY，否则会出现异常
                .setFtResource(new QrResource().setDrawStyle(DrawStyle.MINI_RECT).setDrawColor(Color.RED))
                .setFtW(ftSize)
                .setFtH(ftSize)
                .setFtX((ftSize - size) / 2)
                .setFtY((ftSize - size) / 2)
                .asFile(prefix + "/ft_geo.png");
        System.out.println(ans);
    }

    /**
     * 前置图实现二维码下方配文字
     *
     * @throws Exception
     */
    @Test
    public void fontFt() throws Exception {
        //  创建一个文字的前置图
        BufferedImage ftImg = GraphicUtil.createImg(500, 600, null);
        Graphics2D g2d = GraphicUtil.getG2d(ftImg);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 500, 500, 500);
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("宋体", Font.BOLD, 22));
        g2d.drawString("欢迎关注公众号：<一灰灰Blog>", 20, 540);
        g2d.setFont(new Font("宋体", Font.PLAIN, 20));
        g2d.setColor(Color.GRAY);
        g2d.drawString(LocalDateTime.now().toString(), 20, 570);

        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setFtResource(new QrResource().setImg(ftImg))
                .setFtFillColor(Color.WHITE)
                .asFile(prefix + "/ft_txt.png");
        System.out.println(ans);
    }

    /**
     * 前置图圆角处理
     *
     * @throws Exception
     */
    @Test
    public void roundFt() throws Exception {
        int size = 500;
        int ftSize = size / 5;
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(size)
                .clearLogoArea(true)
                .setFtResource(new QrResource().setImg("bg.png").setPicStyle(PicStyle.ROUND))
                .setFtW(ftSize).setFtH(ftSize)
                .setFtX((ftSize - size) / 2)
                .setFtY((ftSize - size) / 2)
                .asFile(prefix + "/ft_round.png");
        System.out.println(ans);
    }

    /**
     * 前置图圆形处理
     *
     * @throws Exception
     */
    @Test
    public void circleFt() throws Exception {
        int size = 500;
        int ftSize = size / 5;
        boolean ans = QrCodeGenV3.of(msg)
                .setSize(size)
                .clearLogoArea(true)
                .setFtResource(new QrResource().setImg("bg.png").setPicStyle(PicStyle.CIRCLE))
                .setFtW(ftSize).setFtH(ftSize)
                .setFtX((ftSize - size) / 2)
                .setFtY((ftSize - size) / 2)
                .asFile(prefix + "/ft_circle.png");
        System.out.println(ans);
    }
}
