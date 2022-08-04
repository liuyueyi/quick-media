package com.github.hui.quick.plugin.test.v3;

import com.github.hui.quick.plugin.base.OSUtil;
import com.github.hui.quick.plugin.base.awt.ImageLoadUtil;
import com.github.hui.quick.plugin.qrcode.v3.constants.BgStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.v3.constants.PicStyle;
import com.github.hui.quick.plugin.qrcode.v3.core.render.QrRenderWrapper;
import com.github.hui.quick.plugin.qrcode.v3.entity.QrResource;
import com.github.hui.quick.plugin.qrcode.v3.req.QrCodeV3Options;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenWrapper;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

/**
 * @author YiHui
 * @date 2022/8/2
 */
public class QrV3GenTest {

    private String prefix = "/tmp";
    private static final String msg = "http://weixin.qq.com/r/FS9waAPEg178rUcL93oH";


    @Before
    public void init() {
        if (OSUtil.isWinOS()) {
            prefix = "c://quick-media";
        }
    }

    @Test
    public void testRender() throws Exception {
        QrCodeV3Options qrCodeV3Options = new QrCodeV3Options();
        String bg = "http://ww1.sinaimg.cn/large/8154e929gy1g8vho8x6r0j20b40b43yl.jpg";
        int size = 500;
        qrCodeV3Options
                .setMsg(msg)
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
                .setBgStyle(BgStyle.PENETRATE)
                .setOpacity(0.5f).complete()
                .build()
        ;

        BufferedImage img = QrRenderWrapper.renderAsImg(qrCodeV3Options);
        System.out.println("over");
    }

    /**
     * 默认的二维码生成
     */
    @Test
    public void defaultQr() {
        try {
            // 生成二维码，并输出为qr.png图片
            boolean ans = QrCodeGenWrapper.of(msg).asFile(prefix + "/dq.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static <T> void scanMatrix(int w, int h, BiConsumer<Integer, Integer> consumer) {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                consumer.accept(x, y);
            }
        }
    }

}
