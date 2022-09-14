package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;

/**
 * logo相关使用姿势
 *
 * @author YiHui
 * @date 2022/9/13
 */
public class QrImgLogoGenTest extends BasicGenTest {
    @Override
    public void init() {
        super.init();
        this.prefix += "/img";
    }

    /**
     * 最基本的logo使用姿势，注意默认场景下会将被logo覆盖的区域全部置空，实现logo嵌入的效果
     *
     * @throws Exception
     */
    @Test
    public void logo() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setLogo(new QrResource().setImg("logo.jpg"))
                .asFile(prefix + "/logo_basic.png");
        System.out.println(ans);
    }

    /**
     * 若希望logo是覆盖在二维码上的，则可以设置
     *
     * @throws Exception
     */
    @Test
    public void logoOverride() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newLogoOptions()
                .setLogo(new QrResource().setImg("logo.jpg"))
                // 下面这个设置，就是不希望logo的周边信息点被清空
                .setClearLogoArea(false)
                .complete()
                .asFile(prefix + "/logo_override.png");
        System.out.println(ans);
    }

    @Test
    public void logoConfig() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newLogoOptions()
                .setLogo(new QrResource().setImg("logo.jpg"))
                // 设置logo的大小，占整个图片的 1/6 宽高
                .setRate(6)
                .setOpacity(0.6f)
                .complete()
                .asFile(prefix + "/logo_rate.png");
        System.out.println(ans);
    }

    /**
     * 非矩形的logo
     *
     * @throws Exception
     */
    @Test
    public void noSquareLogo() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setLogo(new QrResource().setImg("yhhlogo.png"))
                .setLogoRate(5)
                .asFile(prefix + "/logo_no_square.png");
        System.out.println(ans);
    }

    /**
     * 绘制一个五角星的logo
     *
     * @throws Exception
     */
    @Test
    public void geoLogo() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setLogo(new QrResource().setDrawStyle(DrawStyle.STAR).setDrawColor(Color.RED))
                .asFile(prefix + "/logo_star.png");
        System.out.println(ans);
    }

    /**
     * logo添加边框
     *
     * @throws Exception
     */
    @Test
    public void logoBorder() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setLogo(new QrResource().setDrawStyle(DrawStyle.STAR).setDrawColor(Color.RED))
                .setLogoBorderColor(ColorUtil.int2color(0xfffefefe))
                .setLogoOutBorderColor(ColorUtil.int2color(0xffc7c7c7))
                .asFile(prefix + "/logo_border.png");
        System.out.println(ans);
    }

    /**
     * 圆角logo
     *
     * @throws Exception
     */
    @Test
    public void roundLogo() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setLogo(new QrResource().setImg("yhhlogo.png").setPicStyle(PicStyle.ROUND))
                .setLogoRate(5)
                .setLogoBorderColor(Color.BLUE)
                .asFile(prefix + "/logo_round.png");
        System.out.println(ans);
    }

    /**
     * 圆形logo
     *
     * @throws Exception
     */
    @Test
    public void circleLogo() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setLogo(new QrResource().setImg("bg.png").setPicStyle(PicStyle.CIRCLE))
                .asFile(prefix + "/logo_circle.png");
        System.out.println(ans);
    }
}
