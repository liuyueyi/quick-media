package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;

/**
 * @author YiHui
 * @date 2022/9/14
 */
public class QrImgOtherGenTest extends BasicGenTest {
    @Override
    public void init() {
        super.init();
        this.prefix += "/img";
    }

    /**
     * 设置背景 + 前置图 + 渲染资源 + 探测图形 + logo 等各元素的二维码
     */
    @Test
    public void multiGen() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                // 开始设置二维码渲染资源
                .newDrawOptions()
                .newRenderResource(1, 1, new QrResource().setImg("jihe/a.png")).build()
                .createSource(1, 3, new QrResource().setImg("jihe/b.png")).build()
                .createSource(3, 1, new QrResource().setImg("jihe/c.png")).build()
                .createSource(2, 1, new QrResource().setImg("jihe/d.png")).build()
                .createSource(2, 3, new QrResource().setImg("jihe/e.png")).build()
                .createSource(3, 2, new QrResource().setImg("jihe/f.png")).build()
                .createSource(2, 2, new QrResource().setImg("jihe/g.png")).build()
                .createSource(4, 3, new QrResource().setImg("jihe/h.png")).build()
                .over()
                .setBgColor(ColorUtil.OPACITY)
                .complete()
                // 开始设置探测图形
                .newDetectOptions()
                .setResource(new QrResource().setImg("jihe/PDP.png"))
                .complete()
                // 开始设置logo
                .newLogoOptions()
                .setLogo(new QrResource().setImg("logo.jpg").setPicStyle(PicStyle.ROUND))
                .setBorderColor(0xfffefefe)
                .setOuterBorderColor(0xffc7c7c7)
                .complete()
                // 开始设置背景图
                .newBgOptions()
                .setBg(new QrResource().setImg("gifs/bgBai.gif"))
                .setBgStyle(BgStyle.FILL)
                .setStartX(690)
                .setStartY(20)
                .setBgW(1870)
                .setBgH(646)
                .setOpacity(0.8f)
                .complete()
                .asFile(prefix + "/gif_merge.gif");
        System.out.println(ans);
    }


    /**
     * 简化的使用方式
     * - 颜色支持直接传入int, string类型(html风格), Color 三种样式传参
     * - 资源路径，支持本地地址 + 网络地址
     * - 简单的资源指定，可以不传入QrResource，而是直接传入资源路径即可
     * - 探测图形/logo/背景/前置图/绘制样式等参数设置，除了使用 newXxxOptions() 开始 complete() 结束之外；也可以直接进行设置对应的参数
     */
    @Test
    public void testSimpleUse() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                // 颜色传参，可以是int, string, color
                // 传入int时，注意最前面两位为透明度，不要省略掉，如下面的若是传入了 0x00ff00，实际上相当于一个全透明的颜色
                .setPreColor(0xff00ff00)
                .setBgColor("#ffff00")
                .setDetectColor(Color.RED)
//                直接传入url，代替下面的使用方式
//                .setLogo(new QrResource().setImg("https://spring.hhui.top/spring-blog/css/images/avatar.jpg"))
                .setLogo("https://spring.hhui.top/spring-blog/css/images/avatar.jpg")
                .asFile(prefix + "/simple.png");
        System.out.println(ans);
    }
}
