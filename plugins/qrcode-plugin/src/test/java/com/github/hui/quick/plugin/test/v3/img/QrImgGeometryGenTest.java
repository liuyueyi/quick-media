package com.github.hui.quick.plugin.test.v3.img;

import com.github.hui.quick.plugin.qrcode.v3.constants.DrawStyle;
import com.github.hui.quick.plugin.qrcode.wrapper.QrCodeGenV3;
import com.github.hui.quick.plugin.test.v3.BasicGenTest;
import org.junit.Test;

import java.awt.*;

/**
 * 几何样式的二维码输出
 *
 * @author YiHui
 * @date 2022/9/9
 */
public class QrImgGeometryGenTest extends BasicGenTest {

    @Override
    public void init() {
        super.init();
        prefix = prefix + "/img";
    }

    @Test
    public void rect() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .setDrawStyle(DrawStyle.RECT)
                .asFile(prefix + "/geo_rect.png");
        System.out.println(ans);
    }

    @Test
    public void roundRect() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .setDrawStyle(DrawStyle.ROUND_RECT)
                .asFile(prefix + "/geo_round_rect.png");
        System.out.println(ans);
    }
    @Test
    public void miniRect() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .setDrawStyle(DrawStyle.MINI_RECT)
                .asFile(prefix + "/geo_mini_rect.png");
        System.out.println(ans);
    }

    @Test
    public void triangle() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .setDrawStyle(DrawStyle.TRIANGLE)
                .asFile(prefix + "/geo_triangle.png");
        System.out.println(ans);
    }

    @Test
    public void diamond() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .setDrawStyle(DrawStyle.DIAMOND)
                .asFile(prefix + "/geo_diamond.png");
        System.out.println(ans);
    }

    @Test
    public void star() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setW(300)
                .setDetectSpecial(true)
                .setDrawStyle(DrawStyle.STAR)
                .setPreColor(Color.RED)
                .build()
                .asFile(prefix + "/geo_star.png");
        System.out.println(ans);
    }

    @Test
    public void hexagon() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setW(300)
                .setDrawStyle(DrawStyle.HEXAGON)
                .asFile(prefix + "/geo_hexagon.png");
        System.out.println(ans);
    }

    @Test
    public void octagon() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setW(300)
                .setDrawStyle(DrawStyle.OCTAGON)
                .asFile(prefix + "/geo_octagon.png");
        System.out.println(ans);
    }

    @Test
    public void circle() throws Exception {
        boolean ans = QrCodeGenV3.of(msg).setSize(300)
                .setDrawStyle(DrawStyle.CIRCLE)
                .asFile(prefix + "/geo_circle.png");
        System.out.println(ans);
    }
}
