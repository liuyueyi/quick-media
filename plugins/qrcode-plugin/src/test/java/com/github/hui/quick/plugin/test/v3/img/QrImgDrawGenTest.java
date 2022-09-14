package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.base.awt.ColorUtil;
import com.github.hui.quick.plugin.qrcode.constants.QuickQrUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.TxtMode;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;

/**
 * 绘制资源
 *
 * @author YiHui
 * @date 2022/9/13
 */
public class QrImgDrawGenTest extends BasicGenTest {
    @Override
    public void init() {
        super.init();
        this.prefix = this.prefix + "/img";
    }

    @Test
    public void preImg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setDrawResource(new QrResource().setImg("love/001.png"))
                .asFile(prefix + "/draw_pre.png");
        System.out.println(ans);
    }

    @Test
    public void preBgImg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newDrawOptions()
                // 设置背景图为透明色
                .setBgColor(ColorUtil.OPACITY)
                .setTransparencyBgFill(false)
                .setRenderResource(new QrResource().setImg("love/001.png"), new QrResource().setImg("overbg/b.png"))
                .complete().asFile(prefix + "/draw_pre_bg.png");
        System.out.println(ans);
    }

    /**
     * 通过一套资源图，输出模板二维码
     *
     * @throws Exception
     */
    @Test
    public void multiResource() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newDrawOptions()
                // 指定 1 * 1 的资源图
                .newRenderResource(new QrResource().setImg("love/01.png"))
                .addResource(new QrResource().setImg("love/003_01.png"))
                .addResource(new QrResource().setImg("love/003_02.png"))
                .addResource(new QrResource().setImg("love/003_03.png"))
                .build()
                // 指定宽 1 ，长 4 的资源图
                .createSource(1, 4, new QrResource().setImg("love/004.png"))
                .build()
                // 指定宽 4 ，长 1 的资源图
                .createSource(4, 1, new QrResource().setImg("love/004_02.png"))
                .build().over()
                .complete()
                .setDetect(new QrResource().setImg("love/01.png"))
                .asFile(prefix + "/draw_love.png");
        System.out.println(ans);
    }


    /**
     * 图片 + 几何图形 + 文字混搭的二维码
     *
     * @throws Exception
     */
    @Test
    public void imgTxtGeo() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newDrawOptions()
                .setPreColor(Color.RED)
                // 指定 1 * 1 的资源图
                .newRenderResource(new QrResource().setImg("love/01.png"))
                .addResource(new QrResource().setDrawStyle(DrawStyle.RECT))
                .addResource(new QrResource().setDrawStyle(DrawStyle.STAR))
                .addResource(new QrResource().setDrawStyle(DrawStyle.TXT).setTxtMode(TxtMode.ORDER).setText("小灰").setDrawColor(Color.BLUE), 6)
                .build().over()
                .complete()
                .setDetect(new QrResource().setImg("love/01.png"))
                .asFile(prefix + "/draw_imgTxtGeo.png");
        System.out.println(ans);
    }

    /**
     * 资源图带缺省区域，如十字架的二维码
     *
     * @throws Exception
     */
    @Test
    public void crossQrImg() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .newDrawOptions()
                .setPreColor(Color.RED)
                .newRenderResource(new QrResource().setDrawStyle(DrawStyle.MINI_RECT))
                .build()
                .createSource(3, 3, new QrResource().setImg("cross.png")).setMiss("0-0,2-0,0-2,2-2")
                .build().over()
                .complete()
                .setDetect(new QrResource().setDrawStyle(DrawStyle.RECT).setDrawColor(Color.RED)).setDetectWhole(false)
                .asFile(prefix + "/draw_cross.png");
        System.out.println(ans);
    }


    /**
     * 文字二维码
     *
     * @throws Exception
     */
    @Test
    public void fullTxt() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(500)
                .setDrawResource(new QrResource().setText(QuickQrUtil.DEFAULT_QR_TXT).setTxtMode(TxtMode.ORDER))
                .setDrawStyle(DrawStyle.TXT)
                .asFile(prefix + "/draw_txt.png");
        System.out.println(ans);
    }

}
